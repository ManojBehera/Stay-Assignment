package com.stayzilla.customlistview.model;


public class Model {

	private static Model sSingleton;

	public static Model getSingleton() {
		if (sSingleton == null) {
			sSingleton = new Model();
		}
		return sSingleton;
	}


	//Staging
	public String baseurl = "http://yts.to/api/v2/";// Testing

    public String MOVIE_LIST = baseurl + "list_movies.json";

	public String AADATA = "data";
    public String MOVIES = "movies";
    public String title = "title";
    public String year = "year";
    public String rating = "rating";
    public String img = "img";
	public final int CONNECTION_TIMEOUT = 120;


}
