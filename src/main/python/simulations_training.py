import simulations_launcher

simulation = [
    "simulationCentralisedTrainingGNN",
    "simulationCentralisedTrainingGNNBasic",
    "simulationCentralisedTrainingMLP"
]

simulations_launcher.run(16, simulation)