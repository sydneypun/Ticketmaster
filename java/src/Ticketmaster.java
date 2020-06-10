/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class Ticketmaster{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public Ticketmaster(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + Ticketmaster.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		Ticketmaster esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new Ticketmaster (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add User");
				System.out.println("2. Add Booking");
				System.out.println("3. Add Movie Showing for an Existing Theater");
				System.out.println("4. Cancel Pending Bookings");
				System.out.println("5. Change Seats Reserved for a Booking");
				System.out.println("6. Remove a Payment");
				System.out.println("7. Clear Cancelled Bookings");
				System.out.println("8. Remove Shows on a Given Date");
				System.out.println("9. List all Theaters in a Cinema Playing a Given Show");
				System.out.println("10. List all Shows that Start at a Given Time and Date");
				System.out.println("11. List Movie Titles Containing \"love\" Released After 2010");
				System.out.println("12. List the First Name, Last Name, and Email of Users with a Pending Booking");
				System.out.println("13. List the Title, Duration, Date, and Time of Shows Playing a Given Movie at a Given Cinema During a Date Range");
				System.out.println("14. List the Movie Title, Show Date & Start Time, Theater Name, and Cinema Seat Number for all Bookings of a Given User");
				System.out.println("15. EXIT");
				
				/*
				 * FOLLOW THE SPECIFICATION IN THE PROJECT DESCRIPTION
				 */
				switch (readChoice()){
					case 1: AddUser(esql); break;
					case 2: AddBooking(esql); break;
					case 3: AddMovieShowingToTheater(esql); break;
					case 4: CancelPendingBookings(esql); break;
					case 5: ChangeSeatsForBooking(esql); break;
					case 6: RemovePayment(esql); break;
					case 7: ClearCancelledBookings(esql); break;
					case 8: RemoveShowsOnDate(esql); break;
					case 9: ListTheatersPlayingShow(esql); break;
					case 10: ListShowsStartingOnTimeAndDate(esql); break;
					case 11: ListMovieTitlesContainingLoveReleasedAfter2010(esql); break;
					case 12: ListUsersWithPendingBooking(esql); break;
					case 13: ListMovieAndShowInfoAtCinemaInDateRange(esql); break;
					case 14: ListBookingInfoForUser(esql); break;
					case 15: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice
	
	public static boolean isValidPhone(String phone_number) throws Exception {
		// See if phone number contains letters and if check number of digits
		long phone = Long.parseLong(phone_number);
		long upper_limit = 1000000000 * 10;
		if (phone < upper_limit && phone > 999999999) {
			return true;
		}
		// Return false if letter/symbol is found or if there are not 10 digits
		return false; 
	}
	
	public static void AddUser(Ticketmaster esql){//1
		try {
			// Get input
			Scanner sc = new Scanner(System.in);
			// Row counter 
			int row_counter = 0; 

			System.out.println("Enter email: ");
			String email = sc.nextLine();
			
			System.out.println("Enter last name: ");
			String lname = sc.nextLine();
			
			System.out.println("Enter first name: ");
			String fname = sc.nextLine();
			
			System.out.println("Enter phone number (10 digits only): ");
			String phone = sc.nextLine();
			// Phone Number Validation 
			if(!isValidPhone(phone)){
				System.out.println("\t Please enter a valid phone number (0123456789): "); 
				phone = sc.nextLine(); 
			}

			System.out.println("Enter password: ");
			String pwd = sc.nextLine();
			
			// VALIDATING EMAIL HERE! 
			String query = ""; 
			query = "SELECT * FROM Users WHERE email = \'" + email + "\'";

			try{ 
				// See if input email matches pre-existing emails in the DB. 
				row_counter = esql.executeQueryAndPrintResult(query); 
			}
			catch(SQLException e){
				System.out.println("Error! Please make another account."); 
				return; 
			}

			if (row_counter > 0){
				System.out.println("The email " + email + " already has a registered account. Please register again."); 
				return; 
			}else{
				// If all is successful, add account to the DB. 
				query = String.format("INSERT INTO Users (email, fname, lname, phone, pwd) VALUES ('%s', '%s', '%s', '%s', '%s');", 
				email, fname, lname, phone, pwd);
			}
			// Execute Query
			esql.executeUpdate(query);
			System.out.println("Successfully added " + fname + " " + lname + "!\n\n");
			
			// Throw error message if DB backfires. 
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void AddBooking(Ticketmaster esql){//2
		// Get input
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter Booking ID: ");
		String bid = sc.nextLine();
		
		System.out.println("Enter the booking status (paid, cancelled, pending): "); 
		String status = sc.nextLine(); 

		System.out.println("Enter Booking date (mm/dd/yy) and time (hh:mm:ss AM/PM): ");
		String bdatetime = sc.nextLine();
		
		System.out.println("Enter number of seats booked: ");
		String seats = sc.nextLine();
		
		System.out.println("Enter Show ID: ");
		String sid = sc.nextLine();
		
		System.out.println("Enter User account email: ");
		String email = sc.nextLine();
		
		// BOOKING CONSTRAINTS ADDED HERE!  
		String[] queries = new String[3]; 
		String query = ""; 
		int row_counter = 0; 
		int error_counter = 0; 

		queries[0] = "SELECT * FROM Bookings WHERE bid = " + bid; 
		queries[1] = "SELECT * FROM Shows WHERE sid = " + sid; 
		queries[2] = "SELECT * FROM Users WHERE email = \'" + email + "\'"; 
		
		for (int i=0; i<3; i++){
			try{
				row_counter = esql.executeQueryAndPrintResult(queries[i]); 
			} catch(SQLException e){
				System.out.println("An error has occurred. Please re-enter your information."); 
				return; 
			}
			// INCREMENTING THE NUMBER OF ERRORS BY CASE
			switch (i){
				case 0: {
					if (row_counter > 0){
						System.out.println("Error, booking id: " + bid + " exists in our system.");
						error_counter++; 
					}
					break; 
				}
				case 1: {
					if (row_counter == 0){
						System.out.println("Error, show with sid: " + sid + " does not exist in our system."); 
						error_counter++; 
					}
					break; 
				}
				case 2: {
					if (row_counter == 0){
						System.out.println("Error, user with email: " + email + " does not exist in our system."); 
						error_counter++; 
					}
					break; 
				}
				}
			}
			if (error_counter > 0){
			System.out.println("Please fix all of the errors and try again."); 
			return; 	
			} else{
				query = String.format("INSERT INTO Bookings (bid, status, bdatetime, seats, sid, email) "
				+ "VALUES ('%s', '%s', '%s', '%s', '%s', '%s');", 
				bid, status , bdatetime, seats, sid, email);
			// Execute Query
			try {
				esql.executeUpdate(query);
				System.out.println("Successfully added Booking ID: " + bid + " (Show ID: " + sid + ") for user: " + email + "!\n\n");
			} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	}
	
	public static void AddMovieShowingToTheater(Ticketmaster esql){//3
		// Add Showing of new Movie for an Existing Theater
		Scanner sc = new Scanner(System.in);
		
		// Verifying from DB before adding movie information
		// Theater ID
		String tid = "";
		// Storing quries 
		String queries1= ""; 
		String queries2 = ""; 
		String queries3 = ""; 
		// Finalized query
		String query = ""; 
		// Row counter for verification 
		int row_counter = 0; 

		System.out.println("Please enter the theater ID: "); 
		tid = sc.nextLine(); 

		query = "SELECT * FROM Theaters WHERE tid = " + tid; 

		try{ 
			// Check if theater exists
			row_counter = esql.executeQueryAndPrintResult(query); 
		} catch(SQLException e){
			System.out.println("There is an error in processing the info to the DB. Please try again.");
			return; 
		}
		// TID exists 
		if (row_counter > 0){
			System.out.println("Theater ID " + tid + " exists."); 
		}
		// TID does NOT exist 
		else if (row_counter == 0){
			System.out.println("Error: Theater ID " + tid + "does NOT exist."); 
			return; 
		}

		// Proceed to add Movie
		System.out.println("Enter Movie ID: ");
		String mvid = sc.nextLine();
		
		System.out.println("Enter Movie title: ");
		String title = sc.nextLine();
		
		System.out.println("Enter Release date: ");
		String rdate = sc.nextLine();
		
		System.out.println("Enter Release country: ");
		String country = sc.nextLine();
		
		System.out.println("Enter description: ");
		String description = sc.nextLine();
		
		System.out.println("Enter duration: ");
		String duration = sc.nextLine();
		
		System.out.println("Enter Language code (ie en, de): ");
		String lang = sc.nextLine();
		
		System.out.println("Enter genre: ");
		String genre = sc.nextLine();
		
		// Execute Query
		try {
			queries1 = String.format("INSERT INTO Movies (mvid, title, rdate, country, description, duration, lang, genre) "
				+ "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');", 
				mvid, title, rdate, country, description, duration, lang, genre);
			esql.executeUpdate(queries1);
			System.out.println("Successfully added Movie ID: " + mvid + "!\n\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
		
		// Add Show
		System.out.println("Enter Show ID: ");
		String sid = sc.nextLine();
		
		System.out.println("Enter Show date: ");
		String sdate = sc.nextLine();
		
		System.out.println("Enter Start time: ");
		String sttime = sc.nextLine();
		
		System.out.println("Enter End time: ");
		String edtime = sc.nextLine();
		
		// Execute Query
		try {
			 queries2 = String.format("INSERT INTO Shows (sid, mvid, sdate, sttime, edtime) VALUES ('%s', '%s', '%s', '%s', '%s');", 
				sid, mvid, sdate, sttime, edtime);
			esql.executeUpdate(queries2);
			System.out.println("Successfully added Shows ID: " + sid + " (Movie ID: " + mvid + ")!\n\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return;
		}
		
		// INSERTING INTO PLAYS
		try {
			queries3 = String.format("INSERT INTO Plays (sid, tid) VALUES ('%s', '%s');", sid, tid); 
			esql.executeUpdate(queries3); 
			System.out.println("Successfully added Play with show " + sid + " and Theater ID " + tid + ")!\n\n"); 
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return;
		}
	}
	
	public static void CancelPendingBookings(Ticketmaster esql){//4
		int bid = -1;
        List<List<String>> result = new ArrayList<List<String>>(); 

        String get_status_query = "Select bid FROM Bookings WHERE status = \'Pending\'";
        //get list of bookings that have pending status
        try{
             result = esql.executeQueryAndReturnResult(get_status_query);
             System.out.println(Arrays.deepToString(result.toArray()));
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return;
        }
        // Then loop through the result and update those entries with the same bid
        for(int i = 0; i < result.size(); ++i){
            // Parse bid string to int
            bid = Integer.parseInt(result.get(i).get(0));
            String update_query = "UPDATE Bookings SET status = \'Cancelled\' WHERE bid = " + bid;
            try{
                esql.executeUpdate(update_query);
            }catch (SQLException e){
                System.out.println("Error updating Booking entry with bid " + bid + ". Please try again later.");
                return;
            }
        }
        System.out.println("Successfully cancelled all pending payments.");
	}
	
	public static void ChangeSeatsForBooking(Ticketmaster esql) throws Exception{//5
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter Show seat ID to be cancelled: ");
		String oldSsid = sc.nextLine();
		
		System.out.println("Enter new Show seat ID: ");
		String newSsid = sc.nextLine();
		
		// check if new seats are available and if same price, then replace
	}
	
	public static void RemovePayment(Ticketmaster esql){//6
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter Booking ID: ");
		String bid = sc.nextLine();

		// set Booking status to cancelled and delete payment from database
	}
	
	public static void ClearCancelledBookings(Ticketmaster esql){//7
		// remove all Bookings with status of cancelled
	}
	
	public static void RemoveShowsOnDate(Ticketmaster esql){//8
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter date: ");
		String sdate = sc.nextLine();
		
		System.out.println("Enter Cinema ID: ");
		String cid = sc.nextLine();

		// Remove Shows on a Given Date at a specific Cinema
	}
	
	public static void ListTheatersPlayingShow(Ticketmaster esql){//9
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter Cinema ID: ");
		String cid = sc.nextLine();
		
		System.out.println("Enter Show Movie ID: ");
		String mvid = sc.nextLine();
		
		// all Theaters in a Cinema Playing a Given Show
		String query = "SELECT T.tid, T.tname "
					 + "FROM   Theaters T, Shows S "
					 + "WHERE  T.cid = " + cid + " AND S.mvid = " + mvid;
		try {
			esql.executeQueryAndPrintResult(query);
		} catch(Exception e) {
			System.out.println("Could not execute query and print result");
			e.printStackTrace();
			return;
		}
	}
	
	public static void ListShowsStartingOnTimeAndDate(Ticketmaster esql){//10
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter Show date: ");
		String sdate = sc.nextLine();
		
		System.out.println("Enter Start time: ");
		String sttime = sc.nextLine();
		
		// all Shows that Start at a Given Time and Date
		String query = "SELECT S.sid "
					 + "FROM   Shows S "
					 + "WHERE  S.sdate = " + sdate + " AND S.sttime = " + sttime;
		try {
			esql.executeQueryAndPrintResult(query);
		} catch(Exception e) {
			System.out.println("Could not execute query and print result");
			e.printStackTrace();
			return;
		}
	}

	public static void ListMovieTitlesContainingLoveReleasedAfter2010(Ticketmaster esql){//11
		// Movie Titles Containing love Released After 2010
		String query = "SELECT M.title "
					 + "FROM   Movies M "
					 + "WHERE  M.rdate > \'2009-12-31\' AND M.title LIKE \'%Love%\'";	// %[L][o][v][e]%  maybe? idk lol
		try {
			esql.executeQueryAndPrintResult(query);
		} catch(Exception e) {
			System.out.println("Could not execute query and print result");
			e.printStackTrace();
			return;
		}
	}

	public static void ListUsersWithPendingBooking(Ticketmaster esql){//12
		// First Name, Last Name, and Email of Users with a Pending Booking
		String query = "SELECT U.fname, U.lname, U.email "
					 + "FROM   Users U, Bookings B "
					 + "WHERE  B.status = \'pending\' AND U.email = B.email";
		try {
			esql.executeQueryAndPrintResult(query);
		} catch(Exception e) {
			System.out.println("Could not execute query and print result");
			e.printStackTrace();
			return;
		}
	}

	public static void ListMovieAndShowInfoAtCinemaInDateRange(Ticketmaster esql){//13
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter Movie ID: ");
		String mvid = sc.nextLine();
		
		System.out.println("Enter Cinema ID: ");
		String cid = sc.nextLine();
		
		System.out.println("Enter beginning date range: ");
		String beginningDate = sc.nextLine();
		
		System.out.println("Enter ending date range: ");
		String endingDate = sc.nextLine();
		
		// Title, Duration, Date, and Time of Shows Playing a Given Movie at a Given Cinema During a Date Range
		String query = "SELECT M.title, M.duration, S.sdate, S.sttime "
					 + "FROM   Movies M, Shows S, Cinemas C "
					 + "WHERE  M.mvid  =" + mvid
					 + "  AND  C.cid   =" + cid
					 + "  AND  S.sdate >" + beginningDate
					 + "  AND  S.sdate <" + endingDate;
		try {
			esql.executeQueryAndPrintResult(query);
		} catch(Exception e) {
			System.out.println("Could not execute query and print result");
			e.printStackTrace();
			return;
		}
	}

	public static void ListBookingInfoForUser(Ticketmaster esql){//14
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter User email: ");
		String email = sc.nextLine();
		
		// Movie Title, Show Date & Start Time, Theater Name, and Cinema Seat Number for all Bookings of a Given User
		String query = "SELECT M.title, B.bdatetime, T.tname, C.sno "
					 + "FROM   Bookings B, Movies M, Theaters T, CinemaSeats C "
					 + "WHERE  B.email = \'" + email + "\'";
		try {
			esql.executeQueryAndPrintResult(query);
		} catch(Exception e) {
			System.out.println("Could not execute query and print result");
			e.printStackTrace();
			return;
		}
	}
	
}