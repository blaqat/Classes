import Food from "./Food";


class FoodDataLoader {
    static loadData(type, groceries){
        Food.foodDict = {};
        switch(type){
            case "sql": {
              let rest;
              rest = {
                get:(f)=>{
                  fetch('/foods')
                  .then(
                    response => response.json() 
                  )//The promise response is returned, then we extract the json data
                  .then (jsonOutput => //jsonOutput now has result of the data extraction
                  {
                      f(jsonOutput)
                  })
                },

                reload:()=>{
                  Food.foodDict = {}
                  rest.get((json)=>{
                    json.forEach((food) => {
                      let id = food.shift()
                      let newFood = new Food(...food, id)
                      groceries.replace(newFood);
                    })
                  })
                },

                delete:(food)=>{
                  fetch('/foods/'+food.id, {method: 'DELETE'}).then(()=>rest.reload()) 
                },

                edit:(food)=>{
                  fetch('/foods/' + food.id, {method: 'PUT', headers: {'Content-Type': 'application/json'},
                      body: JSON.stringify({calories: food.calories, tot_fat: food.totalFat, sat_fat: food.saturatedFat, tran_fat: food.transFat, protein: food.proteins, carbs: food.carbs})
                  }).then(()=>rest.reload())
                },

                create:(food, category)=>{
                  fetch('/foods', {method: 'POST', headers: {'Content-Type': 'application/json'},
                      body: JSON.stringify({calories: food.calories, tot_fat: food.totalFat, sat_fat: food.saturatedFat, tran_fat: food.transFat, protein: food.proteins, carbs: food.carbs, food_name: food.name, category_name: category})
                  }).then(()=>rest.reload())
                }
              }

              rest.reload();

              return rest;
            }
              
            case "json": {
                let data = [ {
                    "name": "steak",
                    "category": "Proteins",
                    "calories": 300,
                    "totalFat": 5.73,
                    "saturatedFat": 2.183,
                    "transFat": 0.182,
                    "protein": 29.44,
                    "carbohydrate": 0.0
                  }, {
                    "name": "ground beef",
                    "category": "Proteins",
                    "calories": 200,
                    "totalFat": 13.1,
                    "saturatedFat": 5.3,
                    "transFat": 0.6,
                    "protein": 15.18,
                    "carbohydrate": 0.0
                  }, {
                    "name": "chicken",
                    "category": "Proteins",
                    "calories": 100,
                    "totalFat": 9.3,
                    "saturatedFat": 2.5,
                    "transFat": 0.1,
                    "protein": 27.14,
                    "carbohydrate": 0.0
                  }, {
                    "name": "fish",
                    "category": "Proteins",
                    "calories": 80,
                    "totalFat": 6.34,
                    "saturatedFat": 1.0,
                    "transFat": 0.0,
                    "protein": 19.84,
                    "carbohydrate": 0.0
                  }, {
                    "name": "soy",
                    "category": "Proteins",
                    "calories": 50,
                    "totalFat": 19.94,
                    "saturatedFat": 2.884,
                    "transFat": 0.0,
                    "protein": 36.49,
                    "carbohydrate": 30.16
                  }, {
                    "name": "orange",
                    "category": "Fruits",
                    "calories": 300,
                    "totalFat": 0.12,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 0.94,
                    "carbohydrate": 11.75
                  }, {
                    "name": "banana",
                    "category": "Fruits",
                    "calories": 200,
                    "totalFat": 0.33,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 1.09,
                    "carbohydrate": 22.84
                  }, {
                    "name": "pineapple",
                    "category": "Fruits",
                    "calories": 100,
                    "totalFat": 0.12,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 0.54,
                    "carbohydrate": 13.12
                  }, {
                    "name": "grapes",
                    "category": "Fruits",
                    "calories": 80,
                    "totalFat": 0.16,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 0.72,
                    "carbohydrate": 18.1
                  }, {
                    "name": "blueberries",
                    "category": "Fruits",
                    "calories": 50,
                    "totalFat": 0.33,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 0.74,
                    "carbohydrate": 14.49
                  }, {
                    "name": "romaine",
                    "category": "Vegetables",
                    "calories": 30,
                    "totalFat": 0.3,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 1.2,
                    "carbohydrate": 3.3
                  }, {
                    "name": "green beans",
                    "category": "Vegetables",
                    "calories": 40,
                    "totalFat": 0.22,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 1.83,
                    "carbohydrate": 6.97
                  }, {
                    "name": "squash",
                    "category": "Vegetables",
                    "calories": 100,
                    "totalFat": 0.2,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 1.2,
                    "carbohydrate": 3.4
                  }, {
                    "name": "spinach",
                    "category": "Vegetables",
                    "calories": 50,
                    "totalFat": 0.4,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 2.9,
                    "carbohydrate": 3.6
                  }, {
                    "name": "kale",
                    "category": "Vegetables",
                    "calories": 10,
                    "totalFat": 0.9,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 4.3,
                    "carbohydrate": 8.8
                  }, {
                    "name": "milk",
                    "category": "Dairy",
                    "calories": 300,
                    "totalFat": 3.9,
                    "saturatedFat": 2.4,
                    "transFat": 0.0,
                    "protein": 3.2,
                    "carbohydrate": 4.8
                  }, {
                    "name": "yoghurt",
                    "category": "Dairy",
                    "calories": 200,
                    "totalFat": 5.0,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 9.0,
                    "carbohydrate": 3.98
                  }, {
                    "name": "cheddar cheese",
                    "category": "Dairy",
                    "calories": 200,
                    "totalFat": 9.0,
                    "saturatedFat": 6.0,
                    "transFat": 0.0,
                    "protein": 7.0,
                    "carbohydrate": 0.0
                  }, {
                    "name": "skim milk",
                    "category": "Dairy",
                    "calories": 100,
                    "totalFat": 0.2,
                    "saturatedFat": 0.1,
                    "transFat": 0.0,
                    "protein": 8.3,
                    "carbohydrate": 12.5
                  }, {
                    "name": "cottage cheese",
                    "category": "Dairy",
                    "calories": 80,
                    "totalFat": 4.3,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 11.12,
                    "carbohydrate": 3.38
                  }, {
                    "name": "bread",
                    "category": "Grains",
                    "calories": 200,
                    "totalFat": 1.1,
                    "saturatedFat": 0.0,
                    "transFat": 0.0,
                    "protein": 4.0,
                    "carbohydrate": 13.8
                  }, {
                    "name": "bagel",
                    "category": "Grains",
                    "calories": 300,
                    "totalFat": 1.7,
                    "saturatedFat": 0.1,
                    "transFat": 0.0,
                    "protein": 13.8,
                    "carbohydrate": 68
                  }, {
                    "name": "pita",
                    "category": "Grains",
                    "calories": 250,
                    "totalFat": 1.7,
                    "saturatedFat": 0.3,
                    "transFat": 0.0,
                    "protein": 6.3,
                    "carbohydrate": 35.2
                  }, {
                    "name": "naan",
                    "category": "Grains",
                    "calories": 210,
                    "totalFat": 3.3,
                    "saturatedFat": 0.1,
                    "transFat": 0.0,
                    "protein": 2.7,
                    "carbohydrate": 16.9
                  }, {
                    "name": "tortilla",
                    "category": "Grains",
                    "calories": 120,
                    "totalFat": 0.5,
                    "saturatedFat": 0.1,
                    "transFat": 0.0,
                    "protein": 1.1,
                    "carbohydrate": 8.5
                  }
                ]
                for(let food of data) {
                    new Food(food.name, food.category, food.calories, food.totalFat, food.saturatedFat, food.transFat, food.protein, food.carbohydrate)
                }
                 break;
            }
            
            default : {
                new Food("steak", "Proteins", 300);
                new Food("ground beef", "Proteins", 200);
                new Food("chicken", "Proteins", 100);
                new Food("fish", "Proteins", 80);
                new Food("soy", "Proteins", 50);


                //Fruits
                new Food("orange", "Fruits", 300);
                new Food("banana", "Fruits", 200);
                new Food("pineapple", "Fruits", 100);
                new Food("grapes", "Fruits", 80);
                new Food("blueberries", "Fruits", 50);

                //Vegetables
                new Food("romaine", "Vegetables", 30);
                new Food("green beans", "Vegetables", 40);
                new Food("squash", "Vegetables", 100);
                new Food("spinach", "Vegetables", 50);
                new Food("kale", "Vegetables", 10);

                //Dairy
                new Food("milk", "Dairy", 300);
                new Food("yoghurt", "Dairy", 200);
                new Food("cheddar cheese", "Dairy", 200);
                new Food("skim milk", "Dairy", 100);
                new Food("cotag cheese", "Dairy", 80);

                //Grains
                new Food("bread", "Grains", 200);
                new Food("bagel", "Grains", 300);
                new Food("pita", "Grains", 250);
                new Food("naan", "Grains", 210);
                new Food("tortilla", "Grains", 120);
            }
        }
    }
}

export default FoodDataLoader;