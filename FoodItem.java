public class FoodItem 
{
  String name;
  int calories=0, carbs=0, protein=0, fat=0;
  public FoodItem (String name, int calories, int carbs, int fat, int protein)
  {
    this.name=name;
    this.calories=calories;
    this.carbs=carbs;
    this.protein=protein;
    this.fat=fat;
  }
  
  public String getName()
  {
    return name;
  }
  
  public int getCalories()
  {
    return calories;
  }
  
  public int getCarbs()
  {
    return carbs;
  }
  
  public int getProtein()
  {
    return protein;
  }
  
  public int getFat()
  {
    return fat;
  }
}