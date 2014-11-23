package net.vikev.android.plates.services;

import java.io.IOException;
import java.net.URISyntaxException;

public interface HttpService {
    String httpGET(String URL) throws IOException, IllegalStateException, URISyntaxException;
}
