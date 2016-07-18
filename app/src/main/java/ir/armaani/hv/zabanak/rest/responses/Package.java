package ir.armaani.hv.zabanak.rest.responses;

import java.util.List;

/**
 * Created by Siamak on 15/07/2016.
 */
public class Package {
    public Integer id;
    public String caption;
    public Integer depend_on_package;
    public String image;
    public List<Word> words;
}
