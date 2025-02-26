class Food {
    static foodDict = {};
    name;
    calories;

    constructor(name, category, calories) {
        this.name = name;
        this.calories = calories;

        if(! (category in Food.foodDict) )
            Food.foodDict[category] = {};

        Food.foodDict[category][name] = this;
    }
}

class GroceryList {
    foods;
    calories;

    constructor(){
        this.foods = [];
        this.calories = 0;
    }

    addItem = function(foodCategory, foodName) {
        let food = Food.foodDict[foodCategory][foodName];
        this.foods.push(food)
        this.calories += food.calories;
    }

    remItem = function(foodName) {
        for(let i = 0; i < this.foods.length; i++){
            let food = this.foods[i];

            if(food.name == foodName){
                this.calories -= food.calories;
                this.foods.splice(i, 1);
                break;
            }
        }
    }
}

//Protiens
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


const userGroceryList = new GroceryList()
var onAddMode = true;
var selectedCategory = "";
var selectedFoodName = "";
var foodCategoriesDropdown;
var menuItemsList;
var selectedItemsList;
var totalCaloriesText;
var addModeButton;

function changeSelectedItem() {
    if( onAddMode ) {
        userGroceryList.addItem(selectedCategory, selectedFoodName);
    } else {
        userGroceryList.remItem(selectedFoodName);
    }

    displayGroceryList();
}

function onSelectMenuItem(foodName) {
    setAddMode(true);
    selectedFoodName = foodName; 
}

function onSelectUserItem(foodName) {
    setAddMode(false);
    selectedFoodName = foodName;
}

function onSelectCategory(categoryName) {
    selectedCategory = categoryName;

    menuItemsList.textContent = ''; // empties list

    Object.values(Food.foodDict[categoryName]).forEach(food => {
        displayItemOn(food.name, menuItemsList);
    })
}

function displayGroceryList() {
    selectedItemsList.textContent = '';

    userGroceryList.foods.forEach(food => {
        displayItemOn(food.name, selectedItemsList);
    })

    totalCaloriesText.textContent = "Total Calories: " + userGroceryList.calories;
}

function displayItemOn(item, element) {
    let option = document.createElement("option");
    option.textContent = item;
    option.value = item;

    element.appendChild(option);    
}

function setAddMode(newAddMode) {
    onAddMode = newAddMode;
    addModeButton.textContent = newAddMode?">>":"<<";
}

function onLoad() {
    foodCategoriesDropdown = document.getElementById("foodCategories");
    menuItemsList = document.getElementById("menuItems");
    selectedItemsList = document.getElementById("selectedItems");
    totalCaloriesText = document.getElementById("totalCalories");
    addModeButton = document.getElementById("pickItem");
    
    Object.keys(Food.foodDict).forEach(category => {
        displayItemOn(category, foodCategoriesDropdown)
    })
    
    displayGroceryList();
}