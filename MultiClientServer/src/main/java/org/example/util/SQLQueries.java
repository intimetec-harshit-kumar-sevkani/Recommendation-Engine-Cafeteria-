package org.example.util;
public class SQLQueries {
    public static final String CHECK_VOTED_ITEM = "SELECT COUNT(*) FROM voted_item WHERE FoodItemId = ? AND DATE(Date) = CURDATE()";
    public static final String INSERT_VOTED_ITEM = "INSERT INTO voted_item (FoodItemId, Vote, Date, IsPrepared, IsDelete) VALUES (?, 0, NOW(), false, false)";
    public static final String UPDATE_VOTE = "UPDATE voted_item SET Vote = Vote + 1 WHERE FoodItemId = ? AND DATE(Date) = CURDATE()";
    public static final String MARK_AS_PREPARED = "UPDATE voted_item SET IsPrepared = true WHERE FoodItemId = ?";
    public static final String GET_MEAL_TYPE_ID = "SELECT Id FROM meal_type WHERE Type = ? AND IsDelete = 0";
    public static final String GET_FOOD_ITEMS_FOR_VOTE = "SELECT fi.* FROM food_item fi JOIN voted_item vi ON fi.Id = vi.FoodItemId WHERE DATE(vi.Date) = CURDATE() AND fi.MealTypeId = ?";
    public static final String GET_ROLL_OUT_ITEMS = "SELECT fi.Id, fi.MealTypeId, fi.Name, fi.Price, fi.IsAvailable, fi.IsDelete, vi.Vote FROM food_item fi JOIN voted_item vi ON fi.Id = vi.FoodItemId WHERE DATE(vi.Date) = CURDATE() AND fi.MealTypeId = ?";
    public static final String GET_PREPARED_FOOD_ITEM_IDS = "SELECT FoodItemId FROM voted_item WHERE IsPrepared = 1 AND DATE(Date) = ?";
    public static final String GET_FOOD_ITEMS_BY_IDS_TEMPLATE = "SELECT * FROM food_item WHERE Id IN (%s) AND IsDelete = 0";

    // Queries for AuthenticationRepository
    public static final String FIND_USER_BY_EMAIL_AND_NAME = "SELECT * FROM user WHERE Email = ? AND Name = ? AND IsDelete = FALSE";
    public static final String FIND_ROLE_BY_ID = "SELECT * FROM role WHERE Id = ? AND IsDelete = FALSE";

    // Queries for FeedbackRepository
    public static final String GET_FOOD_ITEM_RATINGS = "SELECT FoodItemId, AVG(Rating) AS average_rating, GROUP_CONCAT(Comment SEPARATOR ', ') AS comments FROM feedback WHERE Date = CURDATE() AND FoodItemId = ? GROUP BY FoodItemId";
    public static final String UPDATE_ITEM_AUDIT = "UPDATE food_item_audit SET Rating = ?, Sentiment = ? WHERE FoodItemId = ?";
    public static final String ADD_FEEDBACK = "INSERT INTO feedback (FoodItemId, UserId, Rating, Comment, Date, IsDelete) VALUES (?, ?, ?, ?, CURDATE(), ?)";

    public static final String INSERT_USER_PROFILE = "INSERT INTO user_profile (UserId, FoodType, SpiceLevel, Originality, SweetTooth) VALUES (?, ?, ?, ?, ?)";
    public static final String SELECT_USER_PROFILE = "SELECT FoodType, SpiceLevel, Originality, SweetTooth FROM user_profile WHERE UserId = ?";

    public static final String SELECT_FOODITEM_NAME_MEALTYPEID = "SELECT Name, MealTypeId FROM food_item WHERE Id = ? AND IsDelete = 0";
    public static final String SELECT_NOTIFICATION_TYPE_ID = "SELECT Id FROM notification_type WHERE Type = ? AND IsDelete = 0";
    public static final String SELECT_MEAL_TYPE = "SELECT Type FROM meal_type WHERE Id = ? AND IsDelete = 0";
    public static final String SELECT_VALID_NOTIFICATIONS = "SELECT n.Id, n.NotificationTypeId, n.Message, n.IsDelete, n.Date, nt.ValidFor " +
            "FROM notification n " +
            "JOIN notification_type nt ON n.NotificationTypeId = nt.Id " +
            "WHERE n.IsDelete = 0 AND nt.IsDelete = 0";
    public static final String INSERT_NOTIFICATION = "INSERT INTO notification (NotificationTypeId, Message, IsDelete, Date) VALUES (?, ?, 0, NOW())";

    // FoodItemAuditRepository SQL query
    public static final String INSERT_FOOD_AUDIT = "INSERT INTO food_item_audit (FoodItemId, Rating, Vote, Sentiment, Prepared) VALUES (?, ?, ?, ?, ?)";

    public static final String DELETE_FOOD_ITEM = "DELETE FROM food_item WHERE Id = ?";
    public static final String INSERT_FOOD_ITEM = "INSERT INTO food_item (MealTypeId, Name, Price, IsAvailable, IsDelete, FoodType, SpiceLevel, Originality, SweetTooth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_FOOD_ITEM = "UPDATE food_item SET MealTypeId = ?, Name = ?, Price = ?, IsAvailable = ?, IsDelete = ?, FoodType = ?, SpiceLevel = ?, Originality = ?, SweetTooth = ? WHERE Id = ?";
    public static final String SELECT_ALL_FOOD_ITEMS = "SELECT * FROM food_item";
    public static final String SELECT_RECOMMENDED_FOOD_ITEMS = "SELECT fi.* " +
            "FROM food_item_audit fia " +
            "JOIN food_item fi ON fia.FoodItemId = fi.Id " +
            "JOIN meal_type mt ON fi.MealTypeId = mt.Id " +
            "WHERE mt.Type = ? " +
            "ORDER BY (fia.Rating + fia.Sentiment) / 2 DESC " +
            "LIMIT ?";
    public static final String SELECT_FOOD_ITEMS_BY_IDS = "SELECT * FROM food_item WHERE Id IN (%s)";
    public static final String SELECT_DISCARDED_FOOD_ITEMS = "SELECT foodItemId FROM discard_item WHERE date >= ?";
    public static final String INSERT_DISCARDED_ITEMS = "INSERT INTO discard_item (foodItemId, date) VALUES (?, ?)";
    public static final String DELETE_FOOD_ITEMS = "DELETE FROM food_item WHERE Id = ?";
}

