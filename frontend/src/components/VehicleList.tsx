import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import Box from '@mui/material/Box';
import styles from './VehicleList.module.css';

type StepVehicles = Record<string, string[]>;

type Props = {
  stepStatuses: string[][];
  stepVehicles?: StepVehicles[];
};

function VehicleList({ stepStatuses, stepVehicles }: Props) {
  if (!stepVehicles || stepVehicles.length === 0) {
    return (
      <Paper elevation={2} sx={{ p: 3, mt: 4 }}>
        <Typography variant="h6" fontWeight={600} color="primary" gutterBottom>
          Pojazdy, które opuściły skrzyżowanie (każdy krok):
        </Typography>
        <List>
          {stepStatuses.map((ids, idx) => (
            <ListItem key={idx} sx={{ py: 0.5, fontSize: 16 }}>
              <strong>Krok {idx + 1}:</strong>{' '}
              {ids.length > 0 ? ids.join(', ') : <span style={{ color: '#888' }}>Brak</span>}
            </ListItem>
          ))}
        </List>
      </Paper>
    );
  }

  const directions = ['NORTH', 'EAST', 'SOUTH', 'WEST'];
  return (
    <Paper elevation={2} sx={{ p: 3, mt: 4 }}>
      <Typography variant="h6" fontWeight={600} color="primary" gutterBottom>
        Kolejki pojazdów na każdym wlocie (każdy krok):
      </Typography>
      {stepVehicles.map((queues, idx) => (
        <Box key={idx} sx={{ mb: 2, borderBottom: '1px solid #eee', pb: 1 }}>
          <Typography variant="subtitle1" fontWeight={500} sx={{ mb: 1 }}>
            Krok {idx + 1}
          </Typography>
          <Box display="flex" flexWrap="wrap" gap={3}>
            {directions.map(dir => (
              <Box key={dir} minWidth={90}>
                <Typography variant="caption" fontWeight={600} color="text.secondary">
                  {dir}
                </Typography>
                <Box display="flex" flexDirection="column" flexWrap="wrap" gap={0.6} mt={0.5} maxHeight={120}>
                  {queues[dir] && queues[dir].length > 0 ? (
                    queues[dir].map((vehicleId) => {
                      const left = stepStatuses[idx]?.includes(vehicleId);
                      return (
                        <div
                          key={vehicleId}
                          title={vehicleId}
                          className={
                            styles.vehicleRect +
                            (left ? ' ' + styles.left : '')
                          }
                        >
                          <span className={styles.vehicleRectShort}>
                            {vehicleId.length > 3 ? vehicleId.slice(0, 3) + '…' : vehicleId}
                          </span>
                        </div>
                      );
                    })
                  ) : (
                    <div className={styles.noVehicles}>brak</div>
                  )}
                </Box>
              </Box>
            ))}
          </Box>
        </Box>
      ))}
    </Paper>
  );
}

export default VehicleList;