//Worked on with Jon

import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeSet;

public class Resolution1 {

 static Scanner kb;
 static TreeComp comp = new TreeComp();
 static TreeSet<TreeSet<String>> sets = new TreeSet<TreeSet<String>>(comp);
//main function
 public static void main(String[] args)  {
  kb = new Scanner(System.in);
  makeSets();
  kb.close();
  //resolve returns true or false, print unsatisfiable or satisfiable accordingly
  if(resolve())  {
   System.out.println("Unsatisfiable");
  }
  else  {
   System.out.println("Satisfiable");
  }
 }
//read in the data from standard input and make a tree of the clauses
 public static void makeSets()  {
  String line;
  String[] lit;
  TreeSet<String> tree;
  while(kb.hasNext())  {
   //Get the line and break it into literals
   line = kb.nextLine();
   tree = new TreeSet<String>();
   lit = line.split(" ");
   //add each literal to a clause tree
   for(int i = 0; i < lit.length; i++)  {
    tree.add(lit[i]);
   }
   //add the clause tree to the tree of clause trees
   sets.add(tree);
  }
 }
//main resolution function. resolves using set-of-support and subsumption
 public static boolean resolve()  {
  TreeSet<TreeSet<String>> old = new TreeSet<TreeSet<String>>(comp), newSets = sets, newest, res;
  while(true)  {
   newest = new TreeSet<TreeSet<String>>(comp);
   //loop through all clauses in new
   for(TreeSet<String> x : newSets)  {
    //loop through all clauses in new that come after the current point in the outer loop, this reduces redundancy
    for(TreeSet<String> y : newSets.tailSet(x, false))  {
     //get the list of clauses that can be created through resolving
     res = resolvants(x, y);
     //problem is unsatisfiable if box can be created by resolving
     if(containsBox(res))  {
      return true;
     }
     //add results of resolving to newest
     newest = union(newest, minus(minus(res, old),newSets));
    }
    //loop through all clauses in old
    //the body of this loop is identical to the above loop
    for(TreeSet<String> y : old)  {
     res = resolvants(x, y);
     if(containsBox(res))  {
      return true;
     }
     newest = union(newest, minus(minus(res, old),newSets));
    }
   }
   //if no new clauses were created through resolving, the problem is satisfiable
   if(newest.isEmpty())  {
    return false;
   }
   //add the new set to old
   old = union(old, newSets);
   //put the newest set into new
   newSets = newest;
   //get rid of any supersets within old and new
   subsumption(old);
   subsumption(newSets);
  }
 }
//find all of the clauses that can be created by resolving two sets
 public static TreeSet<TreeSet<String>> resolvants(TreeSet<String> x, TreeSet<String> y)  {
  TreeSet<TreeSet<String>> res = new TreeSet<TreeSet<String>>(comp);
  //loop through every literal in the first set
  for(String z : x)  {
  //in both of these cases, it is important to check that neither set contains both the positive and negative literal
  //if it did resolve on such a set, it would break the murder rule by allowing a literal to remove the same literal from the other family
  //we can do it this way because we know that such a resolution will never be beneficial
   //if the literal is negative check the other set for the positive version
   if(z.charAt(0) == '~')  {
    if(y.contains(z.substring(1)) && !y.contains(z) && !x.contains(z.substring(1)))  {
     res.add(resolveOn(x, y, z));
    }
   }
   //if the literal is positive check the other set for the negative version
   else  {
    if(y.contains("~" + z) && !y.contains(z) && !x.contains("~" + z))  {
     res.add(resolveOn(x, y, z));
    }
   }
  }
  return res;
 }
//resolve two sets on the given literal
 public static TreeSet<String> resolveOn(TreeSet<String> x, TreeSet<String> y, String z)  {
  TreeSet<String> w = new TreeSet<String>();
  //add all literals from both to a third set
  w.addAll(x);
  w.addAll(y);
  //remove the negative and positve versions of the literal from the combined set
  if(z.charAt(0) == '~')  {
   w.remove(z);
   w.remove(z.substring(1));
  }
  else  {
   w.remove(z);
   w.remove("~" + z);
  }
  //return the unioned set
  return w;
 }
//return if the set contains the empty set
 public static boolean containsBox(TreeSet<TreeSet<String>> res)  {
  return res.contains(new TreeSet<String>());
 }
//return the union of two sets
 public static TreeSet<TreeSet<String>> union(TreeSet<TreeSet<String>> x, TreeSet<TreeSet<String>> y)  {
  TreeSet<TreeSet<String>> z = new TreeSet<TreeSet<String>>(comp);
  z.addAll(x);
  z.addAll(y);
  return z;
 }
//remove all elements present in set y from set x
 public static TreeSet<TreeSet<String>> minus(TreeSet<TreeSet<String>> x, TreeSet<TreeSet<String>> y)  {
  for(TreeSet<String> z : y)  {
   x.remove(z);
  }
  return x;
 }
//remove all clauses from a set which subsume another clause in the set
 public static void subsumption(TreeSet<TreeSet<String>> set)  {
  TreeSet<TreeSet<String>> toRemove = new TreeSet<TreeSet<String>>(comp);
  for(TreeSet<String> x : set)  {
   //only loop through the clauses which come after x
   //because of the ordering of the tree, only the sets which come after are smaller, and therefor possibly subsets
   for(TreeSet<String> y : set.tailSet(x, false))  {
    if(x.containsAll(y))  {
     toRemove.add(x);
    }
   }
  }
  //remove all supersets which were found
  set.removeAll(toRemove);
 }
 
}

//Comparator class used for tree construction
class TreeComp implements Comparator<TreeSet<String>>  {

 @Override
 public int compare(TreeSet<String> x, TreeSet<String> y) {
  //sort trees by size
  if(x.size() > y.size())  {
   return 1;
  }
  else if(x.size() < y.size())  {
   return -1;
  }
  //if same size, sort by toString
  else  {
   return x.toString().compareTo(y.toString());
  }
 }
 
}
