import simulations_launcher

simulation = [
    "simulationTestGNN",
    "simulationTestGNNMoving",
    "simulationTestGNNTWo",
    "simulationTestGNNBasic",
    "simulationTestGNNMovingBasic",
    "simulationTestMLP",
    "simulationTestMLPTwo",
    "simulationTestMLPMoving"
]

simulations_launcher.run(64, simulation)