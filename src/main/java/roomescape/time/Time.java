package roomescape.time;

import lombok.Data;

@Data
public class Time {
    private Long id;

    private String time;

    public Time(){}

    public Time (Long id, String time){
        this.id = id;
        this.time = time;
    }

    public Time (String time){
        this.time = time;
    }

}

