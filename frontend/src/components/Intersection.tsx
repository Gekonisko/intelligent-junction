import { useEffect, useState, useRef } from 'react';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import styles from './Intersection.module.css';

type StepVehicles = Record<string, string[]>;

type Props = {
  step: string[][];
  stepVehicles?: StepVehicles[]; 
};

function Intersection({ step, stepVehicles }: Props) {
  const [currentStep, setCurrentStep] = useState(0);
  const [lights, setLights] = useState<Record<string, 'green' | 'red' | 'yellow'>>({
    NORTH: 'green',
    EAST: 'red',
    SOUTH: 'red',
    WEST: 'green',
  });
  const prevLights = useRef<Record<string, 'green' | 'red' | 'yellow'>>({
    NORTH: 'green',
    EAST: 'red',
    SOUTH: 'red',
    WEST: 'green',
  });
  const yellowTimeout = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    setCurrentStep(0);
    setLights({ NORTH: 'green', EAST: 'red', SOUTH: 'red', WEST: 'green' });
    prevLights.current = { NORTH: 'green', EAST: 'red', SOUTH: 'red', WEST: 'green' };
  }, [step]);

  useEffect(() => {
    if (yellowTimeout.current) {
      clearTimeout(yellowTimeout.current);
      yellowTimeout.current = null;
    }
    const leftVehicles = step[currentStep] || [];
    const vehiclesMap = stepVehicles?.[currentStep] || {};
    const vehicleToDir: Record<string, string> = {};
    Object.entries(vehiclesMap).forEach(([dir, ids]) => {
      ids.forEach(id => { vehicleToDir[id] = dir; });
    });
    const greenDirs = new Set(leftVehicles.map(id => vehicleToDir[id]).filter(Boolean));
    const newLights: Record<string, 'green' | 'red'> = {
      NORTH: greenDirs.has('NORTH') ? 'green' : 'red',
      EAST: greenDirs.has('EAST') ? 'green' : 'red',
      SOUTH: greenDirs.has('SOUTH') ? 'green' : 'red',
      WEST: greenDirs.has('WEST') ? 'green' : 'red',
    };
    const yellowDirs: string[] = [];
    (['NORTH', 'EAST', 'SOUTH', 'WEST'] as const).forEach(dir => {
      if (lights[dir] !== newLights[dir]) {
        yellowDirs.push(dir);
      }
    });
    if (yellowDirs.length > 0) {
      setLights(prev => ({ ...prev, ...Object.fromEntries(yellowDirs.map(d => [d, 'yellow'])) }));
      yellowTimeout.current = setTimeout(() => {
        setLights(prev => ({ ...prev, ...Object.fromEntries((['NORTH', 'EAST', 'SOUTH', 'WEST'] as const).map(d => [d, newLights[d]])) }));
        prevLights.current = { ...newLights };
      }, 700);
    } else {
      setLights(newLights);
      prevLights.current = { ...newLights };
    }
  }, [currentStep, step, stepVehicles]);

  const vehicles = stepVehicles?.[currentStep] || {};

  const maxNorth = vehicles['NORTH']?.length || 0;
  const maxSouth = vehicles['SOUTH']?.length || 0;
  const maxWest = vehicles['WEST']?.length || 0;
  const maxEast = vehicles['EAST']?.length || 0;
  const laneCarSize = 38 + 16;
  const minSize = 420;
  const vertical = Math.max(maxNorth, maxSouth) * laneCarSize + 120;
  const horizontal = Math.max(maxWest, maxEast) * laneCarSize + 120;
  const gridHeight = Math.max(minSize, vertical);
  const gridWidth = Math.max(minSize, horizontal);

  function renderLane(dir: string, vehicles: Record<string, string[]>, lights: Record<string, 'green' | 'red' | 'yellow'>) {
    const queue = vehicles[dir] || [];
    const left = step[currentStep] || [];
    const isVertical = dir === 'NORTH' || dir === 'SOUTH';
    const leaving = queue.filter(id => left.includes(id));
    const waiting = queue.filter(id => !left.includes(id));
    let orderedQueue: string[];
    if (dir === 'NORTH' || dir === 'WEST') {
      orderedQueue = [...waiting, ...leaving];
    } else {
      orderedQueue = [...leaving, ...waiting];
    }
    const renderVehicles = orderedQueue.map((vehicleId) => {
      const isLeaving = left.includes(vehicleId);
      let targetDir = '';
      if (stepVehicles && stepVehicles[currentStep]) {
      }
      return (
        <div
          key={vehicleId}
          className={styles.vehicleRect + (isLeaving ? ' ' + styles.leaving : '')}
          title={vehicleId}
          style={{ fontSize: 13, width: 'auto', minWidth: 38, maxWidth: 120, padding: '0 8px', whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden', display: 'flex', alignItems: 'center', justifyContent: 'center', flexDirection: 'column' }}
        >
          <span>{vehicleId}</span>
          {targetDir && (
            <span style={{ fontSize: 11, color: '#ffd600', fontWeight: 700, letterSpacing: 1 }}>{targetDir}</span>
          )}
        </div>
      );
    });
    const light = (
      <div
        key="light"
        style={{
          width: 32, height: 32, borderRadius: '50%',
          background: lights[dir] === 'green' ? 'limegreen' : lights[dir] === 'yellow' ? 'yellow' : 'red',
          border: '3px solid #222', margin: isVertical ? '8px auto 0 auto' : '0 0 0 8px', transition: 'background 0.5s',
        }}
      />
    );
    let children;
    if (dir === 'NORTH' || dir === 'WEST') {
      children = [...renderVehicles, light];
    } else {
      children = [light, ...renderVehicles];
    }
    return (
      <div className={styles['lane'] + ' ' + styles['lane' + dir.charAt(0) + dir.slice(1).toLowerCase()] }>
        <div style={{
          display: 'flex',
          flexDirection: isVertical ? 'column' : 'row',
          alignItems: 'center',
          justifyContent: isVertical ? (dir === 'NORTH' ? 'flex-end' : 'flex-start') : (dir === 'WEST' ? 'flex-end' : 'flex-start'),
          width: isVertical ? '100%' : undefined,
          height: isVertical ? undefined : '100%',
          gap: 4,
        }}>
          {children}
        </div>
      </div>
    );
  }

  return (
    <Paper elevation={3} sx={{ p: 4, mb: 4, mt: 2 }}>
      <Typography variant="h5" align="center" fontWeight={600} color="primary" gutterBottom>
        Wizualizacja skrzyżowania (animacja)
      </Typography>
      <div
        className={styles.intersectionGrid}
        style={{ width: gridWidth, height: gridHeight }}
      >
        {renderLane('NORTH', vehicles, lights)}
        {renderLane('SOUTH', vehicles, lights)}
        {renderLane('WEST', vehicles, lights)}
        {renderLane('EAST', vehicles, lights)}
        <div className={styles.intersectionCenter}></div>
      </div>
      <Typography sx={{ minHeight: 40, fontSize: 20, mt: 3, transition: 'all 0.5s' }} align="center">
        <strong>Krok {currentStep + 1}:</strong>{' '}
        {step[currentStep]?.length > 0
          ? `Pojazdy, które opuściły skrzyżowanie: ${step[currentStep].join(', ')}`
          : 'Nikt nie przejechał'}
      </Typography>
      <Typography sx={{ mt: 2, color: '#888', fontSize: 14 }} align="center">
        {currentStep + 1} / {step.length} kroków
      </Typography>
      <Box display="flex" justifyContent="center" sx={{ width: 340, mx: 'auto' }}>
        <Button
          variant="contained"
          color="primary"
          size="large"
          sx={{ mt: 3, px: 6, py: 1.5, fontWeight: 600, borderRadius: 2 }}
          onClick={() => setCurrentStep((s) => Math.min(s + 1, step.length - 1))}
          disabled={currentStep >= step.length - 1}
        >
          Dalej
        </Button>
      </Box>
    </Paper>
  );
}

export default Intersection;