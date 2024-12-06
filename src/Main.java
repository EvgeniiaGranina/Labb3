import java.sql.*;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/Users/EvgeniiaGranina/Database/SQLite/java24evgeniiagranina.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void printActions() {
        System.out.println("\nChoose:\n");
        System.out.println("0  - Turn off\n" +
                "1  - Show all recipes\n" +
                "2  - Show all ingredients\n" +
                "3 - Add a new recipe\n" +
                "4  - Add a new ingredient\n" +
                "5 - Update a recipe\n" +
                "5  - Delete a recipe\n" +
                "6  - Show a list of all choices.");
    }

    private static void inputRecipeDelete(){
        System.out.println("Enter the id of the recipe to be deleted: ");
        int inputId = scanner.nextInt();
        recipeDelete(inputId);
        scanner.nextLine();
    }

    private static void recipeDelete(int id) {
        String sql = "DELETE FROM recipe WHERE recipeId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            System.out.println("You have removed the recipe with id " + id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void selectAllIngredients() {
        String ingredientTable = " SELECT * FROM ingredient ";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet ingredientSet = stmt.executeQuery(ingredientTable);

            while (ingredientSet.next()) {
                System.out.println(ingredientSet.getInt("ingredientId") + "\t" +
                        ingredientSet.getString("ingredientName") + "\t" +
                        ingredientSet.getString("ingredientAmount") + "\t" +
                        ingredientSet.getInt("ingredientPrice") + "\t" +
                        ingredientSet.getInt("ingredientRecipeId"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void selectAllRecipes(){
        String recipeTable = " SELECT * FROM recipe ";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet recipeSet    = stmt.executeQuery(recipeTable);

            while (recipeSet.next()) {
                System.out.println( recipeSet.getInt("recipeId") + "\t" +
                        recipeSet.getString("recipeName") + "\t" +
                        recipeSet.getString("recipeCategory"));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void recipeSelectValue(){
        String sql = " SELECT recipe.recipeId, recipe.recipeName, ingredient.ingredientName, ingredient.ingredientAmount, ingredient.ingredientPrice " +
                " FROM recipe" +
                " INNER JOIN ingredient on recipe.recipeId = ingredientRecipeId ";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt("recipeId") +  "\t" +
                        rs.getString("recipeName") + "\t" +
                        rs.getString("ingredientName") + "\t" +
                        rs.getString("ingredientAmount") + "\t" +
                        rs.getInt("ingredientPrice"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void recipeInsert(String name, String category) {

        String sql = " INSERT INTO recipe(recipeName, recipeCategory) " +
                " VALUES(?,?) ";
        try{
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.executeUpdate();
            System.out.println("You have added a new recipe");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void ingredientInsert(String name, String amount, int price, int recipeId) {

        String sql = " INSERT INTO ingredient(ingredientName, ingredientAmount, ingredientPrice, ingredientRecipeId) " +
                " VALUES(?,?,?,?) ";
        try{
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, amount);
            pstmt.setInt(3, price);
            pstmt.setInt(4, recipeId);
            pstmt.executeUpdate();
            System.out.println("You have added a new ingredient");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        boolean quit = false;
        printActions();
        while(!quit) {
            System.out.println("\nSelect (5 to show selection):");
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 0:
                    System.out.println("\nShuts down...");
                    quit = true;
                    break;

                case 1:
                    selectAllRecipes();
                    break;

                case 2:
                    selectAllIngredients();
                    break;

                case 3:
                    System.out.println("Inter the recipe name");
                    String recipeName = scanner.nextLine();

                    System.out.println("Inter the recipe category");
                    String recipeCategory = scanner.nextLine();

                    recipeInsert(recipeName, recipeCategory);
                    break;

                case 4:
                    System.out.println("Inter the ingredient name");
                    String ingredientName = scanner.nextLine();

                    System.out.println("Inter the ingredient amount");
                    String ingredientAmount = scanner.nextLine();

                    System.out.println("Inter the ingredient price");
                    int ingredientPrice = scanner.nextInt();

                    System.out.println("Inter the recipe id");
                    int recipeId = scanner.nextInt();
                    ingredientInsert(ingredientName, ingredientAmount, ingredientPrice, recipeId);
                    break;

                case 5:
                    recipeSelectValue();

                case 6:
                    inputRecipeDelete();
                    break;

                case 7:
                    printActions();
                    break;
            }
        }

    }

}
