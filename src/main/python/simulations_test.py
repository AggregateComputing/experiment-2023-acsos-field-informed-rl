import simulations_launcher

simulation = [
    "simulationTestGNN",
    "simulationTestGNNMoving",
    "simulationTestGNNTwo",
    "simulationTestGNNBasic",
    "simulationTestGNNMovingBasic",
    "simulationTestGNNTwoBasic",
    "simulationTestMLP",
    "simulationTestMLPTwo",
    "simulationTestMLPMoving"
]

simulations_launcher.run(64, simulation, 50)