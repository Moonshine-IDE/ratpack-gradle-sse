package domain;

public class User {
  private String username;
  private int identifier;
  private String firstName;
  private String lastName;

  public String getUsername(){
    return username;
  }

  public void setUsername(String username){
    this.username = username;
  }

  public int getIdentifier(){
    return identifier;
  }

  public void setIdentifier(int identifier){
    this.identifier = identifier;
  }

  public String getFirstName(){
    return firstName;
  }

  public void setFirstName(String firstName){
    this.firstName = firstName;
  }

  public String getLastName(){
    return lastName;
  }

  public void setLastName(String lastName){
    this.lastName = lastName;
  }
}
