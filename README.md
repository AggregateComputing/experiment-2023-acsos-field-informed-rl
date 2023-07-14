# Field-Informed Reinforce Learning (Experiments Repository)
This repository contains the code for the experiments of the paper "Field-informed Reinforcement Learning of  Collective Tasks with Graph Neural Networks" submitted to ACSOS 2023.

The code leverages [Alchemist](http://alchemistsimulator.github.io/) as the simulation framework and [PyTorch Geometric](https://pytorch-geometric.readthedocs.io/en/latest/) as the Graph Neural Network (GNN) library.

## Project structure
The project is structured as follows:
- charts: contains the charts generated and used in the paper
- snapshots: contains the snapshots of the network produced during the training
- src: contains the source code of the project, it is divided into:
    - python: contains the script for launching the test and the training phase
    - scala: contains the code of the simulation and the implementation of Field Informed RL
    - yaml: contains the description of the simulations performed
- test: contains the data extracted during the test phase
- export: contains the data extracted during the training phase
## Getting started
This section highlights the steps to run the experiments.
You can both use the provided VM or install the dependencies on your machine.

### Installation (local)
For running the experiments, you need to have installed:
- A working JDK 11 
- A working pyenv -> [guide](https://github.com/pyenv/pyenv)

Otherwise, you can use the provided [VM](https://doi.org/10.6084/m9.figshare.22634581).

Steps:
- local cloning the repository
```bash
git clone https://github.com/AggregateComputing/experiment-2023-acsos-field-informed-rl
```
- point to the cloned repository
- install the python version required:
```
pyenv install 3.9.6
```
- create a virtual environment
```
pyenv virtualenv 3.9.6 gnn-experiment
```
- activate the virtual environment
```
pyenv activate gnn-experiment
```
- install the dependencies
```
pip install -r requirements.txt
```
Now you are ready to run the experiments, and reproducing the results.

### Smoke test
To check that everything is working, you can run the following command:
```bash
./gradlew runSimulationCentralisedTrainingGNNGraphic
```
This should open a window with the alchemist simulation.
At this point, click P to start the simulation.
If everything is working, you should see the agents moving in the simulation.

## Step-by-step guide
This section highlights the steps to run the experiments, and reproduce the results.
We divide the experiments into three phases:
- training: the phase where the agents learn the task, here we use different models and seed configurations.
- testing: given the best trained model for each configuration, we test it on different seed configurations.
- plotting: we plot the results of the testing phase.

You can run the experiments in each order because we already integrate the results in the repository.
### Training
This will run the training process using the three algorithms highlighted in the paper.
```bash
python src/python/simulations_traning.py
```
This is really time-consuming (in modern machines, it can take around 2 days).
The results will be stored in the export folder and the network snapshots in the snapshots folder.
If you want to see the graphs of the training process, you can run the following command:
```bash
./gradlew runSimulationCentralisedTrainingGNNGraphic
# or
./gradlew runSimulationCentralisedTrainingMLPGraphic
# or
./gradlew runSimulationCentralisedTrainingGNNBasicGraphic
```

### Testing
Similarly to the training phase, you can run the testing phase using the following command:
```bash
python src/python/simulations_test.py
```
This will run, for each model (i.e., GNN, MLP, GNN informed), three scenario: fixed-phenomena, moving phenomena, and multi-phenomena.
For each scenario, we run the test on 64 different seeds.
The result will be stored in the test folder.
To see the GUI of the test, you can run the following command:
```bash
## Fixed-phenomena
./gradlew runSimulationTestGNNGraphic
# or
./gradlew runSimulationTestMLPGraphic
# or
./gradlew runSimulationTestGNNBasicGraphic
## Moving phenomena
./gradlew runSimulationTestGNNMovingGraphic
# or
./gradlew runSimulationTestGNNMovingBasicGraphic
# or
./gradlew runSimulationTestGNNTwoGraphic
## Multi-phenomena
./gradlew runSimulationTestGNNTwoGraphic
# or
./gradlew runSimulationTestMLPTwoGraphic
# or
./gradlew runSimulationTestGNNTwoBasicGraphic
```
Each simulation lasts 200 simulated time units. To start a simulation click P.
### Plotting
For plotting the results, you can run the following command:
```bash
python plotter.py
python plotter-test.py
```
this will generate the charts in the charts folder.
## Result (testing phases)
The GNN informed is the best model, followed by the GNN only. The MLP informed is the worst model, followed by the MLP only.
More details are available in the paper.
In the following, we report the results of the experiments using GIFs.
### Informed
<p align="center">

<img width=32% src="https://github.com/AggregateComputing/experiment-2023-acsos-field-informed-rl/assets/23448811/25412f05-69a2-4869-b9dc-8c9a8706aaee"/>
<img width=32% src="https://github.com/AggregateComputing/experiment-2023-acsos-field-informed-rl/assets/23448811/dab17177-89a3-42ae-819e-5e30fd5b4cc0"/>
<img width=32% src="https://github.com/AggregateComputing/experiment-2023-acsos-field-informed-rl/assets/23448811/3e184a16-12f6-4cb1-9b50-7620b5b752f4"/>
</p>

### GNN (only)
<p align="cent3er">
<img width=32% src="https://github.com/AggregateComputing/experiment-2023-acsos-field-informed-rl/assets/23448811/4c8ba367-a0e1-434c-8da5-a787c7997978"/>
<img width=32% src="https://github.com/AggregateComputing/experiment-2023-acsos-field-informed-rl/assets/23448811/b946325b-5b22-4aff-8731-2c0107bc9216"/>
<img width=32% src="https://github.com/AggregateComputing/experiment-2023-acsos-field-informed-rl/assets/23448811/da93d4e1-62e6-497c-ac65-b7e03d5da830"/>

</p>

### MLP (informed) -- Fail
<img width=32% src="https://github.com/AggregateComputing/experiment-2023-acsos-field-informed-rl/assets/23448811/02f5ca92-6691-4fcd-9bcb-e266899377fb"/>

