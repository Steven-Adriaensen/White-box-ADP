# White Box ADP 
This project contains the source code used in research article: <br />
"*Towards a White Box Approach to Automated Algorithm Design*" (Adriaensen et al, 2016)

Feel free to use this code in your own research.
If you do, we kindly ask you to acknowledge this, by citing the following article:

[1] Steven Adriaensen, and Ann Nowé. "Towards a White Box Approach to Automated Algorithm Design." IJCAI, 2016.

@inproceedings{adriaensen2016towards, <br />
  title={Towards a White Box Approach to Automated Algorithm Design}, <br />
  author={Adriaensen, Steven and Now{\'e}, Ann}, <br />
  booktitle={IJCAI}, <br />
  year={2016} <br />
}

In summary, this paper formulates the Algorithm Design Problem (ADP) as a sequential decision process.
Rather than making difficult design choices (e.g. numerical parameters, data-structures, algorithmic or other implementation choices in general) 
prior to execution, we start executing an algorithm with open design choices. Whenever the next instruction depends on the decision
made for any of these a choice point is reached and an agent is queried for a decision. A special feedback instruction is
used to reward the agent during execution and allows us to specify the desirability of any execution path. 
The solution to the ADP is then an agent maximizing the acumulated reward for any given input. 

Please refer to this article for more detailed information.

**Content**:
- src/wb/ (contains the source code, some key files are indicated below)
  - adp/  (classes representing Algorithm Design Problems)
    - bench/ (implementations of the benchmark ADP's used in experiments of [1, Section 6])
      - Benchmark1.java
      - Benchmark2.java
  - solver/ (solvers for the ADP, they return an agent trained to make the open design choices in a given adp)
    - BB-URS.java (the black-box optimizer used in the experiments of [1, Section 6] as naive baseline)
    - WB.java (a prototype white-box optimizer described in [1, Section 5])
  - agent/ (agent implementations, in particular those used in [1, Section 6], see below)
    - URS.java
	- PURS.java
  - test/
    - Example.java (gives an example of how to use the solvers to solve a given ADP and test the performance of the resulting agent)
- WBADP-eclipse.zip: Archive to import as project in Eclipse IDE 

More information can be found in the documentation of each of the source files.