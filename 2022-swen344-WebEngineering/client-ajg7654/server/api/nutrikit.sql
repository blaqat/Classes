DROP TABLE IF EXISTS foods, categories, goals;

CREATE TABLE categories (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(30)
);

CREATE TABLE foods (
    id   SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(30),
    category_id INT NOT NULL REFERENCES categories(id),
    calories INT,
    tot_fat FLOAT,
    sat_fat FLOAT,
    tran_fat FLOAT,
    protein FLOAT,
    carbs FLOAT
);

CREATE TABLE goals (
    id SERIAL PRIMARY KEY NOT NULL,
    food_prop VARCHAR(30),
    goal FLOAT
);

INSERT INTO goals(food_prop, goal)
VALUES('calories', 2000);
INSERT INTO goals(food_prop, goal)
VALUES('totalFat', 98);
INSERT INTO goals(food_prop, goal)
VALUES('saturatedFat', 20);
INSERT INTO goals(food_prop, goal)
VALUES('transFat', 78);
INSERT INTO goals(food_prop, goal)
VALUES('proteins', 50);
INSERT INTO goals(food_prop, goal)
VALUES('carbs', 275);

    
INSERT INTO categories(name)
VALUES('Proteins'); 
INSERT INTO categories(name)
VALUES('Fruits');
INSERT INTO categories(name)
VALUES('Vegetables'); 
INSERT INTO categories(name)
VALUES('Dairy'); 
INSERT INTO categories(name)
VALUES('Grains');  

INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('steak', 1, 300, 5.73, 2.183, 0.182, 29.44, 0.0);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('ground beef', 1, 200, 13.1, 5.3, 0.6, 15.18, 0.0);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('chicken', 1, 100, 9.3, 2.5, 0.1, 27.14, 0.0);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('fish', 1, 80, 6.34, 1.0, 0.0, 19.84, 0.0);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('soy', 1, 50, 19.94, 2.884, 0.0, 36.49, 30.16);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('orange', 2, 300, 0.12, 0.0, 0.0, 0.94, 11.75);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('banana', 2, 200, 0.33, 0.0, 0.0, 1.09, 22.84);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('pineapple', 2, 100, 0.12, 0.0, 0.0, 0.54, 13.12);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('grapes', 2, 80, 0.16, 0.0, 0.0, 0.72, 18.1);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('blueberries', 2, 50, 0.33, 0.0, 0.0, 0.74, 14.49);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('romaine', 3, 30, 0.3, 0.0, 0.0, 1.2, 3.3);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('green beans', 3, 40, 0.22, 0.0, 0.0, 1.83, 6.97);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('squash', 3, 100, 0.2, 0.0, 0.0, 1.2, 3.4);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('spinach', 3, 50, 0.4, 0.0, 0.0, 2.9, 3.6);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('kale', 3, 10, 0.9, 0.0, 0.0, 4.3, 8.8);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('milk', 4, 300, 3.9, 2.4, 0.0, 3.2, 4.8);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('yoghurt', 4, 200, 5.0, 0.0, 0.0, 9.0, 3.98);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('cheddar cheese', 4, 200, 9.0, 6.0, 0.0, 7.0, 0.0);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('skim milk', 4, 100, 0.2, 0.1, 0.0, 8.3, 12.5);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('cottage cheese', 4, 80, 4.3, 0.0, 0.0, 11.12, 3.38);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('bread', 5, 200, 1.1, 0.0, 0.0, 4.0, 13.8);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('bagel', 5, 300, 1.7, 0.1, 0.0, 13.8, 68);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('pita', 5, 250, 1.7, 0.3, 0.0, 6.3, 35.2);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('naan', 5, 210, 3.3, 0.1, 0.0, 2.7, 16.9);
INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
VALUES('tortilla', 5, 120, 0.5, 0.1, 0.0, 1.1, 8.5);