# Field-Informed Reinforce Learning (Experiments Repository)
This repository contains the code for the experiments of the paper "Field-informed Reinforcement Learning of  Collective Tasks with Graph Neural Networks" submitted to ACSOS 2023.

The code leverages [Alchemist]() as the simulation framework and [PyTorch Geometric](https://pytorch-geometric.readthedocs.io/en/latest/) as the GNN library.

## Installation

- A working JDK 11 installation
- A working pyenv installation -> [guide]()

### Steps
## Commands
To run both the training and the evaluation of the model, run the following command:
```bash
python launch_simulation.py
```
## Result
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

