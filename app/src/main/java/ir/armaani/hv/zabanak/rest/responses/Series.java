package ir.armaani.hv.zabanak.rest.responses;

import java.util.List;

/**
 * Created by Siamak on 15/07/2016.
 */
public class Series {
    public Integer id;
    public String caption;
    public String provider;
    public Integer download_count;
    public String image;
    public List<Package> packages;
}
