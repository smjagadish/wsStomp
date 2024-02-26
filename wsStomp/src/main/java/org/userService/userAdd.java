package org.userService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class userAdd {
    private Set<String> sessionSet = new HashSet<>();

    public void addSession(String sessionID)
    {
        if(!sessionSet.contains(sessionID))
        {
            sessionSet.add(sessionID);
        }
    }
    public Set<String> getSessions()
    {
        return sessionSet;
    }
    public void removeSession(String id)
    {
        if(id!=null)
        {
            if(sessionSet.contains(id))
                sessionSet.remove(id);
            else
            {

            }
        }
    }

}
