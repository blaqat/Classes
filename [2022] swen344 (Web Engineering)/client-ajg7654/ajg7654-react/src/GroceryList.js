import Food from "./Food";

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

            if(food.name === foodName){
                this.calories -= food.calories;
                this.foods.splice(i, 1);
                break;
            }
        }
    }

    replace = function(newFood) {
        for(let i = 0; i < this.foods.length; i++){
            let food = this.foods[i];

            if(food.name === newFood.name){
                this.foods[i]=newFood
            }
        }
    }

    getFood = function(foodName) {
        for(let i = 0; i < this.foods.length; i++){
            let food = this.foods[i];

            if(food.name === foodName){
                return food
            }
        }
    }

    getTotalProps = function(){
        let total = {
            saturatedFat: 0,
            transFat: 0,
            calories: 0,
            proteins: 0,
            carbs: 0
        }

        this.foods.forEach(food=>{
            for(let [k,v] of Object.entries(food)){
                total[k] = parseInt(total[k]) + parseInt(v)
            }
        })

        total.totalFat = total.saturatedFat + total.transFat;

        return total;
    }
}

export default GroceryList