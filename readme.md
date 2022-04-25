### Problem statement ###
The main aim of the problem is to encode set of N numbers into *true* or *false* values, such that as many brackets as
possible after performing **or** operation for all numbers in one bracket would become *true*.

Example:  
(1, 2, 3)  
(-3, 0, -2)  
(-0, -2, -1)  

If the numbers are encoded in the following way:  
0 = false  
1 = true  
2 = false  
3 = true  

The result would be:  
(true **or** false **or** true) = true  
(false **or** false **or** true) = true  
(true **or** true **or** false) = true  

Minus sign negates the value of the encoded number. It is important to remember that **0** and **-0**
are different numbers.