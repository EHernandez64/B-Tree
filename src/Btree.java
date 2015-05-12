//Enrique Hernandez
//CS 4311
//Exercise 9 Btree
//3/11/2015

/* Btree.java 
   

*/


// Wrapper class
// PBR = Pass By Reference
 class PBR {
	public Object obj;
	
	PBR(Object n){
		obj = n;
	}
}



class Node_type {
  public int count;
  public char key[] = new char[Btree.MAX+1];
  public Node_type branch[] = new Node_type[Btree.MAX+1];
};

public class Btree {

   public static void main (String args[]) {
   
     if (args.length == 0)
       io = new IO(System.in,System.out);
     else
       io = new IO(args[0],System.out);
     interact();

   }

static final int MAX = 4;
static final int MIN = 2;
static final int MAXD = 10;
static final int MAXWD = 50;

// typedef char Key_type; just use char in Java


static int step=0;
static int cnt[] = new int[MAXD];
static int levels;
static int loc[][] = new int[MAXD][MAXWD];
static Node_type ptrs[][] = new Node_type[MAXD][MAXWD];
static Node_type root;
static IO io;



static void Error(String msg)
{
  io.println(msg);            // note that there is an io.print(String) also
}



static void DFS(Node_type p, int depth)
{
  int i;

  if (p != null) {
    if (depth>levels) levels=depth;
    ptrs[depth][cnt[depth]] = p;
    cnt[depth]++;
  
    for (i=0; i<=p.count; i++)
      DFS(p.branch[i],depth+1); 
  }
}


static void interact()
{
  char key;
  root = null;

  for (;;) {
    io.print("> ");
    String s = io.getLine(); 
    if (s.length() > 0) {
      char cmd = s.charAt(0);
      switch (cmd) {
      case 'q' : return;
      case 'h' : io.println("h(elp; q(uit; i(nsert x; d(elete x; p(rint\n");
                 break;
      case 'i' : key = s.charAt(2);
                 io.println("INSERT: "+ key); 
                 root=Insert(key,root);
                 break;     
      case 'd' : key = s.charAt(2);
                 io.println("DELETE: "+key); 
                 // uncomment when Delete is converted
                 root=Delete(key,root);
                 break;
      case 'p' : io.println("B-TREE:"); 
                 // uncomment when PrinAllNodes is converted
                 PrintAllNodes(root);
                 break;
      } // end switch
    } // end if
  } // end for
}




static void ListNode (int depth, int cnt, Node_type p){
	int i;
	
	io.print(depth+ " " + cnt);
	for(i =1; i <= p.count; i++)
		io.println(p.key[i] + "");
	io.println("");
}


static void SetLoc(){
	int i, depth, pos, base, first, last, dist, x;
	
	depth = levels;
	pos = 0;
	for(i = 0; i < cnt[depth]; i++){
		loc[depth][i] = pos;
		pos += (ptrs[depth][i].count)*2+2;
	}
	for (depth=levels-1; depth>=0; depth--){
		  base = 0;
		  for (i=0; i<cnt[depth]; i++){
		     first = base;
		     last  = first+(ptrs[depth][i].count);
		     dist = (loc[depth+1][last] - loc[depth+1][first] + 2*ptrs[depth+1][last].count)/2;
		     loc[depth][i] = loc[depth+1][first]+dist-ptrs[depth][i].count;
		     base += ptrs[depth][i].count+1;
		  }
		}
}



static void MoveRight(Node_type p,int k)
{
  int c;
  Node_type t;

  t = p.branch[k];
  for (c=t.count; c>0; c--){
     t.key[c+1]=t.key[c];
     t.branch[c+1]=t.branch[c];
  }
  t.branch[1] = t.branch[0];  
  t.count++;
  t.key[1]=p.key[k];
  t=p.branch [k-1];  
  p.key[k]=t.key[t.count];
  p.branch[k].branch[0]=t.branch[t.count];
  t.count--;
}



static void MoveLeft(Node_type p,int k)
{
  int c;
  Node_type t;

  t = p.branch[k-1];
  t.count++;
  t.key[t.count] = p.key[k];
  t.branch[t.count] = p.branch[k].branch[0];
  t = p.branch[k];
  p.key[k] = t.key[1];
  t.branch[0] = t.branch[1];
  t.count--;
  for (c=1; c<=t.count; c++){
    t.key[c] = t.key[c+1];
    t.branch[c] = t.branch[c+1];
  }
}


static void Combine(Node_type p,int k)
{
  int c;
  Node_type q; 
  Node_type l;

  q = p.branch[k];
  l = p.branch[k-1];  
  l.count++;          
  l.key[l.count] = p.key[k];
  l.branch[l.count] = q.branch[0];
  for (c=1; c<=q.count; c++){  
    l.count++;
    l.key[l.count] = q.key[c];
    l.branch[l.count] = q.branch[c];
  }
  for (c=k; c<p.count; c++){  
    p.key[c] = p.key[c+1];
    p.branch[c] = p.branch[c+1];
  }
  p.count--;
 // free(q);    //Not needed in Java
}



static void Restore(Node_type p, int k)
{
  if (k==0)                
    if (p.branch[1].count > MIN)
      MoveLeft(p,1);
    else
      Combine(p,1);
  else 
    if (k==p.count)    
      if (p.branch[k-1].count > MIN)
        MoveRight(p,k);
      else
        Combine(p,k);
    else 
      if (p.branch[k-1].count> MIN) 
        MoveRight(p,k);
      else 
        if (p.branch[k+1].count> MIN)
          MoveLeft(p,k+1);
        else 
          Combine(p,k);
}


static void Remove(Node_type p,int k)
{
  int i;  
  for (i=k+1; i<=p.count; i++){
    p.key[i-1] = p.key[i];
    p.branch[i-1] = p.branch[i];
  }
  p.count--;
}


static void Successor(Node_type p, int k)
{
  Node_type q;  
  for (q=p.branch[k]; q.branch[0] != null; q=q.branch[0]) 
    ;
  p.key[k]=q.key[1];
}

static void PushIn(char x,Node_type xr,Node_type p, int k)  
{
   int i;               
	
   for (i = p.count; i > k; i--) {
     p.key[i+1] = p.key[i];
     p.branch[i+1] = p.branch[i];
   }
   p.key[k+1] = x;
   p.branch[k+1] = xr;
   p.count++;
}                 


// How can parameters be call-by-ref in Java? Not allowed to make global.
// Try to think of a simple - but good OO - way to handle all of these 
// call-by-ref params in the source. You do not want to make a mistake 
// or the program will be impossible to debug.

static void PrintNode(Node_type p, int depth, int i, PBR pos)  // CALL-BY-REF
{
  int j;
  while (((int)pos.obj)<loc[depth][i]) {
    io.print(" ");
    pos.obj = (int)pos.obj + 1;
  }
  io.print("[");
  for (j=1; j<p.count; j++){
    io.print(p.key[j] + ", ");
    pos.obj = (int)pos.obj + 2;
  }
  io.print(p.key[j] + "]");
  pos.obj = (int)pos.obj + 3;
}


static void PrintTop(Node_type p, int depth, int i, PBR pos, PBR child) // CALL-BY-REF
{
  int j,strt,mid;

  mid = loc[depth][i]+p.count;
  for (j=0; j<=p.count; j++) {
    strt = loc[depth+1][(int)child.obj]+1;
    if (strt>=mid)
      strt += 2*p.branch[j].count-2;

    while (((int)pos.obj)<strt) {
      io.print(" ");
      pos.obj = (int)pos.obj + 1;
    }
  
    if (strt<=mid)
      io.print("/");
    else
      io.print("\\");
    pos.obj = (int)pos.obj + 1;
    child.obj = (int)child.obj + 1;
  }
}



static void PrintLine(Node_type p, int depth, int i, PBR pos, PBR child) // CALL-BY-REF
{
  int strt,stop,mid;

  strt = loc[depth+1][(int)child.obj]+2;
  stop = loc[depth+1][(int)child.obj+p.count]+p.branch[p.count].count*2-2;
  mid = loc[depth][i]+p.count;
  child.obj = (int)child.obj + p.count+1;
  while (((int)pos.obj)<strt) {
    io.print(" ");
    pos.obj = (int)pos.obj + 1;
  }
  while (((int)pos.obj)<=stop) {
    if ((int)pos.obj==mid) 
      io.print("|");
    else
      io.print("_");
    pos.obj = (int)pos.obj + 1;
  }
}


static void PrintAllNodes(Node_type root)
{
  int i,depth;
  
  PBR pos, child;

  if (root==null)
    io.println("EMPTY");
  else {
    for (depth=0; depth<MAXD; depth++) 
      cnt[depth]=0;
    levels=0; 
    DFS(root,0);
    SetLoc();
    io.println("L");
    for (depth=0; depth<=levels; depth++) {
      io.print(depth + "");
      pos = new PBR(-1);
      for (i=0; i<cnt[depth]; i++)
        PrintNode(ptrs[depth][i],depth,i,pos);
      io.println("");
      if (depth < levels) {
        pos.obj = -2;
        child= new PBR(0);
        for (i=0; i<cnt[depth]; i++)
          PrintLine(ptrs[depth][i],depth,i,pos,child);
        io.println("");
        pos.obj = -2;
        child.obj = 0;
        for (i=0; i<cnt[depth]; i++)
          PrintTop(ptrs[depth][i],depth,i,pos,child);
        io.println("");
      } 
    } 
    io.println("");
  }
}


static boolean SeqSearch(char target,Node_type p, PBR k)  // CALL-BY-REF
{
  if (target < p.key[1]) {  
    k.obj = 0;
    return false;
  } 
  else {
    k.obj = p.count;
    while ((target<p.key[(int)k.obj]) && (int)k.obj > 1) {
      k.obj = (int)k.obj - 1;
      step++; 
    }
    return (target==p.key[(int)k.obj]);
  }
}



static void Split(char x, Node_type xr, Node_type p, int k, PBR y, PBR yr) // CALL-BY-REF
{
  int i;              
  int median;

  if (k <= MIN)
    median = MIN;
  else
    median = MIN + 1;
  
  yr.obj = new Node_type ();
  for (i = median+1; i <= MAX; i++) {  
    ((Node_type)yr.obj).key[i-median] = p.key[i];
    ((Node_type)yr.obj).branch[i-median] = p.branch[i];
  }
  ((Node_type)yr.obj).count = MAX - median;
  p.count = median;
  if (k <= MIN) 
   PushIn(x,xr,p,k);
  else
    PushIn(x,xr,(Node_type)yr.obj,k - median);
  y.obj = (char)p.key[p.count];
  ((Node_type)yr.obj).branch[0] = p.branch[p.count];
   p.count--;
}


static boolean PushDown(char newkey,Node_type p,PBR x, PBR xr) // CALL-BY-REF
{
  PBR k = new PBR (0);   
  if (p == null) {  
    x.obj = newkey;
    xr.obj = null;
    return true;
  } 
  else {        
    if (SeqSearch(newkey,p,k))
      Error("inserting duplicate key");
    if (PushDown(newkey,p.branch[(int)k.obj],x,xr))
      if (p.count < MAX) {
        PushIn((char)x.obj,(Node_type)xr.obj,p,(int)k.obj);
        return false;
      } 
    else {
      Split((char)x.obj,(Node_type)xr.obj,p,(int)k.obj,x,xr);
      return true;
    }
    return false;
  }
}



static boolean RecDelete(char target,Node_type p)
{
  PBR k = new PBR (0);  
  boolean found;

  if (p==null)
    return false;    
  else {
    found=SeqSearch(target,p,k);
    if (found)
      if (p.branch[(int)k.obj-1] != null) {     
        Successor(p,(int)k.obj);  
        if (!(found=RecDelete(p.key[(int)k.obj],p.branch[(int)k.obj]))) Error("Key not found.");
      } else
          Remove(p,(int)k.obj); 
    else                 
      found=RecDelete(target,p.branch[(int)k.obj]);
      if (p.branch[(int)k.obj] != null)
        if (p.branch[(int)k.obj].count < MIN)
          Restore(p,(int)k.obj);
    return found;
  }
}


static Node_type Delete(char target, Node_type root)
{
   Node_type p,t;      
	
   t = root;
   if (!RecDelete(target, t))
     Error("Target was not in the B-tree.");
   else 
     if (root.count == 0) {  
       p = root;
       root = root.branch[0];
       //free(p);
     }
  return root;
}


// C version Node_type *Insert(Key_type newkey, Node_type *root)


static Node_type Insert(char newkey, Node_type root)
{
  
  PBR x = new PBR (null);   
  PBR xr = new PBR (null);        
  Node_type p; 
  boolean pushup; 

  pushup = PushDown(newkey,root,x,xr);
  if (pushup) {  
    p = new Node_type(); 
    p.count = 1;
    p.key[1] = (char)x.obj;
    p.branch[0] = root;
    p.branch[1] = (Node_type)xr.obj;
    return p;
  }
  
  return root;
}

} // end Btree class
