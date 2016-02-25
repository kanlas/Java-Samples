import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class FoodDiary
{
  static int calorieTotal, carbP, fatP, proteinP, carbG, fatG, proteinG, calT=0, carbT=0, fatT=0, proteinT=0, choiceW;
  
  static ArrayList<FoodItem> database = new ArrayList<FoodItem>();
  static ArrayList<FoodItem> diary = new ArrayList<FoodItem>();
  
  static Scanner kb = new Scanner(System.in);
  public static void main(String[] args) throws IOException
  {
    
    // Add foods into database
    Scanner dbin = new Scanner(new FileInputStream("database.txt"));
    for (int i=0; i<33; i++)
    {
      String inName = dbin.next();
      int inCal = dbin.nextInt();
      int inCarb = dbin.nextInt();
      int inFat = dbin.nextInt();
      int inProt = dbin.nextInt();
      FoodItem entry = new FoodItem(inName, inCal, inCarb, inFat, inProt);
      database.add(entry);
    }
    dbin.close();
                               
    calorieTotal=2000;
    carbP=55;
    fatP=25;
    proteinP=20;
    carbG=275;
    fatG=55;
    proteinG=100;
    System.out.println ("Welcome to your food journal.");
    System.out.println ("The standard recommended caloric intake is 2000 calories.");
    System.out.println ("Would you like to change this? Enter 1 for yes or 0 for no");
    int yes = kb.nextInt();
    if (yes == 1)
    { 
      changeCalories();
    }
    System.out.println();
    System.out.println ("The government recommended breakdown of macronutrients is:");
    System.out.println("45%-65% carbohydrates, 20%-35% fat, 10%-25% protein.");
    System.out.println("Would you like to change from the default 55/25/20 breakdown? Enter 1 for yes");
    int yes2=kb.nextInt();
    if (yes2 == 1)
      changeMacros();
    
    if (yes == 1 || yes2 == 1)
      recalculate();
    
    System.out.println();
    System.out.println("Is your goal to gain, lose, or maintain weight?");
    System.out.println("Enter 1 for gain, 2 for lose, 3 for maintain:");
    choiceW=kb.nextInt();
    
    for(;;)
    {
    callMenu();
    int answer = kb.nextInt();
    if (answer == 1)
      enterFood();
    else if (answer == 2)
      removeFood();
    else if (answer == 3)
      displayDetails();
    else if (answer == 4)
      changeCalories();
    else if (answer == 5)
      changeMacros();
    else if (answer == 6)
      makeSuggestion();
    else if (answer == 7)
      submitFood();
    else if (answer == 8)
      break;
    else
      System.out.println("Invalid option number.");
    if (answer == 4 || answer == 5)
      recalculate();
    }
    System.out.println("Thanks for using the Food Diary!");
  }
  
  public static void callMenu ()
  {
    System.out.println ();
    System.out.println ();
    System.out.println("Pick an option:");
    System.out.println("1: Enter a food.");
    System.out.println("2: Remove a food.");
    System.out.println("3: View current details.");
    System.out.println("4: Change calorie goal.");
    System.out.println("5: Change macronutrient goal.");
    System.out.println("6: Suggest a meal for me!");
    System.out.println("7: Submit a new food item.");
    System.out.println("8: Quit.");
  
  
  }
  
  public static void changeCalories ()
  {
     System.out.println ("Please enter your caloric intake goal: ");
      calorieTotal=kb.nextInt();
      System.out.println ("Your new caloric intake goal is "+calorieTotal+" calories per day.");
  }
  
  public static void changeMacros()
  {
    int flag=1;
    while (flag==1)
      {
      System.out.println("Please enter your goal carbohydrate percentage:");
      carbP=kb.nextInt();
      System.out.println("Please enter your goal fat percentage:");
      fatP=kb.nextInt();
      System.out.println("Please enter your goal protein percentage:");
      proteinP=kb.nextInt();
      if (carbP+fatP+proteinP != 100)
        System.out.println("Your percentages did not add up to 100.");
      else flag=0;
      }
      System.out.println("Your new breakdown is "+carbP+"/"+fatP+"/"+proteinP+".");
  }
  
  public static void displayDetails ()
  {
    System.out.println ("Your caloric goal is: "+calorieTotal);
    System.out.println ("Your macronutrient breakdown is: "+carbP+"/"+fatP+"/"+proteinP+".");
    System.out.println ("Your current foods eaten are: ");
    System.out.println ("Name \t\tCalories \t\tCarbs(g) \tFat(g) \tProtein(g)");
    for (int i=0; i<diary.size(); i++)
    {
      FoodItem cur = diary.get(i);
      if (cur.getName().length() > 10)
       System.out.println (cur.getName()+"\t"+cur.getCalories()+"\t\t"+cur.getCarbs()+"\t"+cur.getFat()+"\t"+cur.getProtein());
      else
      System.out.println (cur.getName()+"\t\t"+cur.getCalories()+"\t\t"+cur.getCarbs()+"\t"+cur.getFat()+"\t"+cur.getProtein());
    }
    System.out.println();
    System.out.println("Totals: \t\t"+calT+"\t\t"+carbT+"\t"+fatT+"\t"+proteinT);
    System.out.println("Remaining: \t"+(calorieTotal-calT)+"\t\t"+(carbG-carbT)+"\t"+(fatG-fatT)+"\t"+(proteinG-proteinT));
  }
  
  public static void recalculate()
  {
    carbG = calorieTotal * carbP / 100 / 4;
    fatG = calorieTotal * fatP / 100 / 9;
    proteinG = calorieTotal * proteinP / 100 / 4;
    System.out.println ("Your new goal totals are "+carbG+ "g carbs, "+fatG+"g fats, and "+proteinG+"g protein.");
  }
  
  public static void removeFood()
  {
    System.out.println("Enter the name of the food to be removed or 'back':");
    String remove = kb.next().toLowerCase();
    if (!remove.equals("back"))  
    {  
      for (int i=0; i<diary.size(); i++)
      {
        FoodItem it = diary.get(i);
        if (it.getName().equals(remove))
        {
          calT-=it.getCalories();
          carbT-=it.getCarbs();
          fatT-=it.getFat();
          proteinT-=it.getProtein();
          diary.remove(i);
          System.out.println(it.getName()+" removed.");
          break;
        }
      }
    }
  }
  
  public static void submitFood()
  {
    System.out.println ("Please enter the food name:");
    String nName = kb.next().toLowerCase();
    System.out.println ("Please enter the total calories:");
    int nCalories = kb.nextInt();
    System.out.println ("Please enter the total carbohydrates (in grams):");
    int nCarbs = kb.nextInt();
    System.out.println ("Please enter the total fat (in grams):");
    int nProtein = kb.nextInt();
    System.out.println ("Pleaes enter the total protein (in grams):");
    int nFat = kb.nextInt();
    FoodItem add = new FoodItem(nName, nCalories, nCarbs, nFat, nProtein);
    database.add(add);
    System.out.println(nName+" submitted.");
  }
  
  public static void enterFood()
  {
    while (true)
    {
      int flag = 0;
      System.out.println ("Enter the food to search for or 'back':");
      String searchFor = kb.next().toLowerCase();
      if (searchFor.equals("back"))
      {
        break;
      }
      for (int i=0; i<database.size(); i++)
      {
        FoodItem it = database.get(i);
        if (it.getName().equals(searchFor))
        {
          System.out.println("Add "+it.getName()+"? Calories: "+it.getCalories());
          System.out.println("Carbs: "+it.getCarbs()+"g. Fat: "+it.getFat()+"g. Protein: "+it.getProtein()+"g.");
          int answer = kb.nextInt();
          if (answer == 1)
          {
            diary.add(it);
            calT+=it.getCalories();
            carbT+=it.getCarbs();
            fatT+=it.getFat();
            proteinT+=it.getProtein();
            System.out.println (it.getName()+" added.");
          }
          else
            System.out.println (it.getName()+" not added.");
          flag=1;
        }
      }
      
      if (flag == 0)
      {
        System.out.println ("Food not found. Please try again.");
      }
    }
  }
  
  
  public static void makeSuggestion ()
  {
    double calorieR, carbR, fatR, proteinR, compare1=0, compare2=0, closest=0, subCal=0, subCarb=0, subFat=0, subProtein=0;
    ArrayList<FoodItem> choices = new ArrayList<FoodItem>();
    calorieR=calorieTotal-calT;
    carbR=carbG-carbT;
    fatR=fatG-fatT;
    proteinR=proteinG-proteinT;
    
    FoodItem choice, option;
    int rand = (int) Math.ceil(Math.random()*12);
    choice=database.get(rand);
    choices.add(database.get(rand));
    carbR-=choice.getCarbs();
    fatR-=choice.getFat();
    proteinR-=choice.getProtein();
    subCal+=choice.getCalories();
    subCarb+=choice.getCarbs();
    subFat+=choice.getFat();
    subProtein+=choice.getProtein();
    int flag=0;
    
    if (choiceW == 1)
    {
      carbR= (carbR*.25)+carbR;
      fatR= (fatR*.25)+fatR;
      proteinR = (proteinR*.25)+proteinR;
    }
    if (choiceW == 2)
    {
      carbR= carbR-(carbR*.25);
      fatR = fatR-(fatR*.25);
      proteinR = proteinR-(proteinR*.25);
    }
    System.out.println ("You have "+calorieR+" calories remaining, consisting of "+carbR+"g carbs, "+fatR+"g fat, and "+proteinR+"g protein.");
    
    while (true) //((carbR-subCarb)>9 || (fatR-subFat)>9 || (proteinR-subProtein)>9)
    {
      flag=0;
      compare1=0; compare2=0;   
      
      
      if (carbR >= fatR && carbR >= proteinR && (carbR-subCarb)>9)
      {
        for (int i=0; i<database.size(); i++)
        {
          option=database.get(i);
          if (i==0) {closest=option.getCarbs(); choice=option;}
          else 
          {
            compare1 = carbR-closest;
            compare2 = carbR-option.getCarbs();
            if (compare2 < compare1 && compare2 > 0 && compare2 < carbR)
            {
              if ((proteinR-option.getProtein())>0 && (fatR-option.getFat())>0)
              {
                closest=option.getCarbs();
                choice=option;
                flag=1;
              }
            }
          }
        } 
      }
      
      
      else if (fatR >= carbR && fatR >= proteinR && (fatR-subFat)>9)
      {
        for (int i=0; i<database.size(); i++)
        {
          option=database.get(i);
          if (i==0) {closest=option.getFat(); choice=option;}
          else 
          {
            compare1 = fatR-closest;
            compare2 = fatR-option.getFat();
            if (compare2 < compare1 && compare2 > 0 && compare2 < fatR)
            {
              if ((proteinR-option.getProtein())>0 && (carbR-option.getCarbs())>0)
              {
                closest=option.getFat();
                choice=option;
                flag=1;
              }
            }
          }
        }
      }
      
      
      else //(proteinR >= carbR && proteinR >= fatR && (proteinR-subProtein)>9)
      {
        for (int i=0; i<database.size(); i++)
        {
          option=database.get(i);
          if (i==0) {closest=option.getProtein(); choice=option;}
          else 
          {
            compare1 = proteinR-closest;
            compare2 = proteinR-option.getProtein();
            if (compare2 < compare1 && compare2 > 0 && compare2 < proteinR)
            {
              if ((carbR-option.getCarbs())>0 && (fatR-option.getFat())>0)
              {
                closest=option.getProtein();
                choice=option;
                flag=1;
              }
            }
          }
        }
      }
      if (flag==1)
      {
        carbR-=choice.getCarbs();
        fatR-=choice.getFat();
        proteinR-=choice.getProtein();
        subCal+=choice.getCalories();
        subCarb+=choice.getCarbs();
        subFat+=choice.getFat();
        subProtein+=choice.getProtein();
        choices.add(choice);
      }
      
      if ((carbR<subCarb) && (fatR<subFat) && (proteinR<subProtein)) break;
    }
    System.out.print ("You could eat a meal of a ");
    for (int i=0; i<choices.size(); i++)
    {
      if (choices.size()==1)
        System.out.println(choices.get(i).getName()+" ");
      else if (i==(choices.size()-1))
        System.out.print("and a "+choices.get(i).getName()+" ");
      else
        System.out.print(choices.get(i).getName()+", ");
    }
    System.out.println("for a total of "+subCal+" calories, "+subCarb+"g carbs, "+subFat+"g fat, "+subProtein+"g protein, ");
    System.out.print("giving you an overall total of "+(subCal+calT)+" calories, "+(subCarb+carbT)+"g carbs, ");
    System.out.println((subFat+fatT)+"g fat, "+(subProtein+proteinT)+"g protein.");
    System.out.println("Your goal totals were "+calorieTotal+" calories, "+carbG+"g carbs, "+fatG+"g fat, and "+proteinG+"g protein.");
  }
}