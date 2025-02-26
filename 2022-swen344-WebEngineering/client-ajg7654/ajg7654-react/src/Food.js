class Food {
    static foodDict = {};
    static getFood = (foodName) => {
        for(let [, v] of Object.entries(Food.foodDict)){
            for(let [,y] of Object.entries(v)){
                if(y.name === foodName)
                    return y;
            }
        }
    }
    name;
    calories;
    totalFat;
    saturatedFat;
    transFat;
    proteins;
    carbs;
    id;

    constructor(name, category, calories, totalFat, saturatedFat, transFat, protein, carbohydrate, id) {
        this.setValues(name, calories, totalFat, saturatedFat, transFat, protein, carbohydrate, id)

        if(! (category in Food.foodDict) )
            Food.foodDict[category] = {};

        Food.foodDict[category][name] = this;
    }

    setValues(name, calories, totalFat, saturatedFat, transFat, protein, carbohydrate, id){
        this.name = name;
        this.calories = calories;
        this.totalFat = totalFat;
        this.saturatedFat = saturatedFat;
        this.transFat = transFat;
        this.proteins = protein;
        this.carbs = carbohydrate;
        this.id = id;
    }

    propsToDVPercent(){
        return {
            saturatedFat: this.saturatedFat/20,
            transFat: this.transFat/78,
            calories: this.calories/2000,
            proteins: this.proteins/50,
            carbs: this.carbs/275,
            totalFat: this.totalFat/98
        }
    }

    propsToDVRating(){
        let dv = this.propsToDVPercent();
        let ratings = {}
        for(let [key, value] of Object.entries(dv)){
            ratings[key] = value < .2? "success":"danger";
        }
        return ratings;
    }

    getProps(){
        return {
            saturatedFat: this.saturatedFat,
            transFat: this.transFat,
            calories: this.calories,
            proteins: this.proteins,
            carbs: this.carbs,
            totalFat: this.totalFat
        }
    }
}


export default Food