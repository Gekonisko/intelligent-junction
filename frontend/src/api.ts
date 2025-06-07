export async function runSimulation(commands: any[]) {
  const res = await fetch("http://localhost:8080/api/simulation", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ commands }),
  });
  return await res.json();
}
