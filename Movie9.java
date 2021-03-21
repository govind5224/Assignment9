package ass9;

import java.awt.List;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.sql.Date;
import java.util.Locale.Category;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class movie
{
	protected int movieId;
	protected String movieName; 
	protected String movieType;
	private Language movieLang;
	protected Date releaseDate;
	protected ArrayList<String> casting;
	protected double rating;
	protected double totalBusinessDone;

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getMovieType() {
		return movieType;
	}

	public void setMovieType(String movieType) {
		this.movieType = movieType;
	}
	
	public Language getMovieLang() {
		return movieLang;
	}

	public void setMovieLang(Language movieLang) {
		this.movieLang = movieLang;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public ArrayList<String> getCasting() {
		return casting;
	}

	public void setCasting(ArrayList<String> casting) {
		this.casting = casting;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getTotalBusinessDone() {
		return totalBusinessDone;
	}

	public void setTotalBusinessDone(double totalBusinessDone) {
		this.totalBusinessDone = totalBusinessDone;
	}

	public movie(int movieId,
	String movieName,
	String movieType,
	String language,
	Date releaseDate,
	ArrayList<String> casting,
	double rating,
	double totalBusinessDone) {
		
		this.movieId=movieId;
		this.movieName=movieName;
		this.movieType=movieType;
		
		movieLang=new Language();
		this.releaseDate=releaseDate;
		this.casting=casting;
		this.rating=rating;
		this.totalBusinessDone=totalBusinessDone;
	}
}




public class Movie9 {
	static Connection con=null;
	static ArrayList<movie> a;
	public static Connection connect() throws ClassNotFoundException, SQLException
	{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","Admin","Admin");
		return con;
	}
	public static void main(String[] args) throws Exception{
		
		Movie9 ms=new Movie9();
		File f= new File("C:\\Users\\acer\\eclipse\\Persistent\\Ass9\\main.txt");
		a=ms.populateMovies(f);
		

		Scanner sc1=new Scanner(System.in);
		ArrayList<movie> a12=ms.getMoviesRealeasedInYear("2015");
		while(true)
		{
			System.out.println("1. Insert Into DB\n2. Add New Movie\n3. Serialize Movies\n4. Deserialize Movie\n5. Find Movies By Year\n6. Find Movies By Actor\n7. Update Movie Rating\n8. Update Business Done\n9. Map Languages and Movies\n10. Exit");
			int opt=Integer.parseInt(sc1.nextLine());
			if(opt==10)
				break;
			switch(opt)
			{
				case 1: ms.allMoviesInDb(a);break;
				
				case 2: ms.addMovie( a);break;
				
				case 3: ms.addMovieToFile();break;
				
				case 4: ms.readMovieFromFile();break;
				
				case 5: System.out.println("Enter Year");
						ArrayList<movie> al= ms.getMoviesRealeasedInYear(sc1.nextLine());
						ms.display(al);
						break;
						
				case 6: System.out.println("Enter Actor");
					ArrayList<movie> al1= ms.getMoviesByActor(sc1.nextLine());
					ms.display(al1);
					break;
				 	    
				case 7:System.out.println("enter the Movie Name");
						ms.updateRatings(5.0,sc1.nextLine() );break;
				
				case 8:System.out.println("enter the Movie Name");
						ms.updateBusiness(500, sc1.nextLine());break;
		
				case 9: System.out.println("Enter Amount");
						Map<Language, Set<movie>> mp= ms.getMoviesByBusiness(Double.parseDouble(sc1.nextLine()));
						break;
						
					
				default : ms.addMovie(a);break;
			}
		}
		
		
	}
	public ArrayList<movie> populateMovies(File file) throws FileNotFoundException, ParseException
	{
		Scanner sc=new Scanner(file);
		ArrayList<movie> l=new ArrayList<movie>();
		while(sc.hasNextLine())
		{
			String s[]=sc.nextLine().split(",");
			int a=Integer.parseInt(s[0]);
			SimpleDateFormat sdf=new SimpleDateFormat("dd/mm/yyyy");
			Date d=Date.valueOf(s[4]);
			int n=Integer.parseInt(s[5]);
			ArrayList<String> cast=new ArrayList<String>();
			for(int i=0;i<n;i++)
			{
				cast.add(s[i+5+1]);
			}
			double b=Double.parseDouble(s[n+5+1]);
			double b1=Double.parseDouble(s[n+5+2]);
			
			 
			l.add(new movie(a,s[1],s[2],s[3],d,cast,b,b1));
		}
		return l;
	}
	public Boolean allMoviesInDb(ArrayList<movie> arr) throws ClassNotFoundException, SQLException
	{
		for (int i = 0; i < arr.size(); i++) {
			movie m=arr.get(i);
			System.out.println(m.getTotalBusinessDone());
			ArrayList<String> s1=m.getCasting();
			String a1="";
			for (int j = 0; j < s1.size(); j++) {
				a1+=s1.get(j);
				a1+=" ";
			}
			
			String  sq="INSERT INTO movies12(movieId,movieName,Category,language,releaseDate,casting,rating,totalBusinessDone)"
	                + "VALUES(?,?,?,?,?,?,?,?)";
			PreparedStatement ps=connect().prepareStatement(sq);
			ps.setInt(1, m.getMovieId());
			ps.setString(2, m.getMovieName());
			ps.setString(3, m.getMovieType());
			ps.setInt(4, m.getMovieLang().getLangID());
			ps.setDate(5, (java.sql.Date) m.getReleaseDate());
			ps.setString(6, a1);
			ps.setDouble(7, m.getRating());
			ps.setDouble(8, m.getTotalBusinessDone());
			boolean a=ps.execute();
			
		
			
		}
		return true;
	}
	public void display(ArrayList<movie> arr)
	{
		System.out.println("Movieid\tMovieName\tcategory\t releseDate\t language\t casting\t rating\t BussinessDone");
		for(int i=0;i<arr.size();i++)
		{
			movie m=arr.get(i);
			System.out.println(m.getMovieId()+"\t"+m.getMovieName()+"\t"+m.getMovieType()+" \t "+m.getReleaseDate()+" \t "+m.getCasting().toString()+" \t "+m.getRating()+" \t "+m.getTotalBusinessDone());
		}
	}
	public void addMovie(ArrayList<movie> m)
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("enter the movie id");
		int n=sc.nextInt();
		sc.nextLine();
		System.out.println("Enter the MovieName");
		String s1=sc.nextLine();
		System.out.println("Enter the category");
		String s2=sc.nextLine();
		System.out.println("Enter the Language");
		String l=sc.nextLine();
		System.out.println("Enter the Date in format YYYY-MM-DD");
		Date d=Date.valueOf(sc.nextLine());
		System.out.println("Number of casts");
		int n1=sc.nextInt();
		sc.nextLine();
		ArrayList<String> al=new ArrayList<String>();
		for (int i = 0; i <n1; i++) {
			System.out.println("Enter the cast"+i);
			al.add(sc.nextLine());
		}
		System.out.println("Enter the rating");
		double d1=sc.nextDouble();
		System.out.println("Enter the totalBusinessDone");
		double d2=sc.nextDouble();
		m.add(new movie(n,s1,s2,l,d,al,d1,d2));
		
	}
	public void addMovieToFile()throws Exception
	{
		File f=new File("D:\\Persistent\\Folder2\\m2.txt");
		FileOutputStream fos=new FileOutputStream(f);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(a);
		System.out.println("Object Inserted Successfully");
		oos.close();
		fos.close();
		
	}
	public ArrayList<movie> readMovieFromFile()
	{
		FileInputStream fis=null;
		ObjectInputStream ois=null;
			try
			{
				File f=new File("Movies.dat");
				
				fis=new FileInputStream(f);
				ois=new ObjectInputStream(fis);
		
				Object object=ois.readObject();
			    ArrayList<Object> objects3 = (ArrayList<Object>) Collections.singletonList(object);
			    
			    ArrayList<movie> al=new ArrayList<movie>();

				for(int i=0;i<objects3.size();i++)
				{
					movie m=(movie)objects3.get(i);
					al.add(m);
				}
				
				ois.close();
				fis.close();
				
			}catch(Exception e){e.printStackTrace();}
			return a;
	}
	public ArrayList<movie> getMoviesRealeasedInYear(String year)
	{		
		ArrayList<movie> mal=new ArrayList<movie>();
		for(int i=0;i<a.size();i++)
		{
			String[] y=a.get(i).getReleaseDate().toString().split("-");
			System.out.println(y[0]);
			if(y[0].equalsIgnoreCase(year))
				mal.add(a.get(i));
		}
		return mal;
	}
	public ArrayList<movie> getMoviesByActor(String actorNames)
	{
		ArrayList<movie> mal=new ArrayList<movie>();
		for(int i=0;i<a.size();i++)
		{
			String[] y=a.get(i).getCasting().toString().split(" ");
			System.out.println(y[0]);
			for (int j = 0; j < y.length; j++) {
				if(y[j].equals(actorNames))
				{
					mal.add(a.get(i));
				}
			}
		}
		return mal;
		
	}
	public void updateRatings(double rating ,String MovieName)
	{
		for(int i=0;i<a.size();i++)
		{
			String name=a.get(i).getMovieName();
			if(name.equals(MovieName))
			{
				a.get(i).setRating(rating);
			}
		}
	}
	public void updateBusiness( double amount,String MovieName) {
		for(int i=0;i<a.size();i++)
		{
			String name=a.get(i).getMovieName();
			if(name.equals(MovieName))
			{
				a.get(i).setTotalBusinessDone(amount);
			}
		}
		
	}
	public Map<Language, Set<movie>> getMoviesByBusiness(double amount)
	{
		Iterator<movie> it=a.iterator();
		
		Map<Language, Set<movie>> mp=new HashMap<Language,Set<movie>>();
			
		Language lang1=new Language(1,"english");
		Language lang2=new Language(2,"hindi");
		Language lang3=new Language(3,"marathi");
		
		Set<movie> eng=new HashSet<movie>();
		Set<movie> hin=new HashSet<movie>();
		Set<movie> mar=new HashSet<movie>();
		
		while(it.hasNext())
		{
			movie m=it.next();
			if(m.getMovieLang().getLangName().equalsIgnoreCase("english") && m.getTotalBusinessDone()>amount)
				eng.add(m);
			if(m.getMovieLang().getLangName().equalsIgnoreCase("hindi") && m.getTotalBusinessDone()>amount)
				hin.add(m);
			if(m.getMovieLang().getLangName().equalsIgnoreCase("marathi") && m.getTotalBusinessDone()>amount)
				mar.add(m);
		}
		
		mp.put(lang1, eng);
		mp.put(lang2, hin);
		mp.put(lang3, mar);
		
		return mp;
		
	}
	
}
