package net.vikev.android.plates.services;

import java.io.IOException;

public interface HttpService {
    String httpGET(String URL) throws IOException;
}
