package mjaroslav.mcmods.example;

import com.google.gson.annotations.SerializedName;

public class ExampleJSON {
	@SerializedName("i_am_a_boolean_copy")
	public boolean iAmABooleanCopy = true;
	@SerializedName("i_am_an_integer_copy")
	public int iAmAnIntegerCopy = 1917;
	@SerializedName("i_am_the_string_copy")
	public String iAmTheStringCopy = "Time is the cage that we created to feel safe.";
}
