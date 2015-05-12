CS 4311 Software Engineering II: Ex9 Re-engineer Btree
======================================================
The key point is: at any given node, the keys to the LEFT are LESS than the
   current key, and the keys to the RIGHT are GREATER.

   This makes it easy to find a key - just go LEFT or RIGHT as necessary until the key
   is found, or the search falls off the tree.

   If the tree is balanced, it only takes O(log n) performance.

   Now, here is a B-tree:

                      [j]
             __________|__________
            /                     \
           [c,f]               [m,r]
       ______|______     ________|________
      /     /       \   /     /           \
     [a,b] [d,e] [g,i] [k,l] [n,p] [s,t,u,x]

   "B" tree does not mean "Binary", it means "Balanced".
   In fact, a B-tree can have many keys per node (or page), and does not have just
   LEFT and RIGHT children. If a node has 2 keys, then it will have 3 children.
   Look at the [m,r] node: the first pointer points to the [k,l] node, whose keys are
   are LESS than "m". The second pointer points to the [n,p] node, whose keys are
   GREATER than "m" but LESS than "r". The third, and final, pointer points to
   the [s,t,u,x] node, whose keys are GREATER than "r".

   How to find the key "p"? Starting from the root, "p" is GREATER than "j", so
   chase the second pointer to [m,r]. Key "p" is GREATER than "m" but LESS than "r",
   so again chase the second pointer to [n,p]. The page is searched and "p" is found.
   This is just like a BST, but nodes have more keys and more pointers. Also note that
   the keys within a node are SORTED in ascending order.
