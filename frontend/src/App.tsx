import { useState } from 'react';
import { runSimulation } from './api';
import Intersection from './components/Intersection';
import VehicleList from './components/VehicleList';
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';


function App() {
  const [commandsText, setCommandsText] = useState('');
  const [stepStatuses, setStepStatuses] = useState<string[][]>([]);
  const [stepVehicles, setStepVehicles] = useState<Record<string, string[]>[]>([]);

  const handleRun = async () => {
    try {
      setStepStatuses([]);
      setStepVehicles([]);
      const parsed = JSON.parse(commandsText);
      const res = await runSimulation(parsed.commands);
      const statuses = res.stepStatuses;
      const vehicles: { vehicleId: string, startRoad: string, endRoad: string }[] = [];
      const stepVehiclesArr: Record<string, string[]>[] = [];
      const stepStatusesArr: string[][] = [];
      const roadMap: Record<string, string> = { north: 'NORTH', south: 'SOUTH', east: 'EAST', west: 'WEST' };
      statuses.forEach((item: any) => {
        if (item.type === 'addVehicle') {
          vehicles.push({ vehicleId: item.vehicleId, startRoad: item.startRoad, endRoad: item.endRoad });
        }
        if (item.type === 'step') {
          const current: Record<string, string[]> = { NORTH: [], EAST: [], SOUTH: [], WEST: [] };
          vehicles.forEach(v => {
            if (v.startRoad && v.vehicleId) {
              const dir = roadMap[v.startRoad.toLowerCase()];
              if (dir) current[dir].push(v.vehicleId);
            }
          });
          stepVehiclesArr.push(current);
          stepStatusesArr.push(item.stepResult.leftVehicles || []);
          if (item.stepResult.leftVehicles) {
            item.stepResult.leftVehicles.forEach((vid: string) => {
              const idx = vehicles.findIndex(v => v.vehicleId === vid);
              if (idx !== -1) vehicles.splice(idx, 1);
            });
          }
        }
      });
      setStepVehicles(stepVehiclesArr);
      setStepStatuses(stepStatusesArr);
    } catch (err) {
      alert('Błąd w JSON');
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 4, minHeight: '100vh', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
      <Paper elevation={4} sx={{ p: 4, mb: 4, width: '100%', maxWidth: 800 }}>
        <Typography variant="h3" align="center" gutterBottom fontWeight={700} color="primary">
          Symulacja Inteligentnych Świateł
        </Typography>
        <Box display="flex" flexDirection="column" alignItems="center" gap={2}>
          <TextField
            label="Wklej JSON z komendami"
            multiline
            minRows={8}
            maxRows={16}
            fullWidth
            value={commandsText}
            onChange={e => setCommandsText(e.target.value)}
            variant="outlined"
            sx={{ mb: 2, fontFamily: 'monospace' }}
          />
          <Button variant="contained" size="large" color="primary" onClick={handleRun} sx={{ px: 6, py: 1.5, fontWeight: 600 }}>
            Uruchom symulację
          </Button>
        </Box>
      </Paper>
      {stepStatuses.length > 0 && (
        <>
          <Intersection step={stepStatuses} stepVehicles={stepVehicles} />
          <VehicleList stepStatuses={stepStatuses} />
        </>
      )}
    </Container>
  );

}

export default App
