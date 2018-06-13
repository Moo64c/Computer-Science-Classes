Cyber Riddles - Task 4
=======================
Amir Arbel

### 1. Under which circumstances the sorter operates correctly?
The purpose of the sorter is simple; black boxes get ejected with a piston, while red boxes reach the end.
When running the simulation without changing any settings in UPPAAL, the sorter will appear to act correctly.

### 2. Under which circumstances the sorter misbehaves?
By querying using the verifier:

**Q1:** ``` E<> B1.end | B2.end | R1.off | R2.off ```

Meaning will B1 or B2 ("black brick 1 or 2") eventually reach the end state, or R1 or R2 ("red brick 1 or 2") eventually reach the off state. If **Q1** is satisfied it means the sorter is not operating correctly.
We can also see this happening with the same settings by adding more black bricks; three were enough for the standard simulation to break by letting a black brick reach the end state.

### 3. Use the functionalities of UPPAAL to explain what happens when the sorter misbehaves.
By asking UPPAAL for a trace that satisfies **Q1**, we get that black bricks reach a point where they either wait for "eject" sync, or just continue on with the flow when the clock is out. This means that if other things are attended to in the system, the sorter will not trigger the piston in time to remove the black brick and bring it to it's correct placement.


### 4. Change the model of the controlling tasks in order to ensure correct sorting of two bricks.
See attached edited.xml file. To fix the model, I've added a mirrored set of conditions that mimic the black brick requirement of a piston; in this case, we can treat it as a "conveyer" on/off switch for the red bricks.
Other option was to make the piston "extend" further to push the red brick to another position (y=2 for red, y=1 for black), or something similar.
As well as running the simulator, I've also added a verifier query to check the correctness of the system, which we want to be satisfied

**Q2:** ```
E<> B1.off & B2.off & R1.end & R2.end ```

Which was satisfied. **Q1** was not satisfied by the new system, as required.
