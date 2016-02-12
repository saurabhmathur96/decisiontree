# decisiontree

An implementation of the ID3 decision tree classification algorithm.

Classes :
- ID3Classifier
- Instance

Data Files :
- car.data : from UCI Machine Learning Data-set Repository

Output from a test run:

```
  high, 3
    2, unacc {unacc=192}
    4, 0
      high, 1
        high, acc {acc=12}
        low, acc {acc=12}
        vhigh, unacc {unacc=12}
        med, acc {acc=12}
      low, 4
        small, acc {acc=8, good=8}
        big, 2
          2, vgood {acc=1, vgood=3}
          3, vgood {acc=1, vgood=3}
          4, vgood {acc=1, vgood=3}
          5more, vgood {acc=1, vgood=3}
        med, acc {acc=6, vgood=6, good=4}
      vhigh, acc {acc=24, unacc=24}
      med, acc {acc=30, vgood=12, good=6}
    more, acc {acc=96, vgood=35, unacc=49, good=12}
  low, unacc {unacc=575}
  med, unacc {acc=180, unacc=357, good=39}

```