package MainServer;

/**
 * Created by saul on 4/1/17.
 */

public class Subjects {
    private int id;
    private String name;
    private String surname;
    private String subject;
    private String email;
    private String psw;
    private String days;
    private String time;

    public Subjects(int id, String name, String surname, String subject, String email, String psw, String days, String time) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.subject = subject;
        this.email = email;
        this.psw = psw;
        this.days = days;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
    
    
}
