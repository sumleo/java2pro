package Utils;


import Configure.ConfigLoader;
import DAO.DataBuffer;
import Model.Earchquake;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class CSVinitial {
    private File file;
    private ArrayList<String> region;

    /**
     * Initial the csv
     */
    public CSVinitial() {
        this.file = new File(ConfigLoader.csvLocation);
    }

    /**
     * Initial the csv data
     */
    public void initial(LocalDate from, LocalDate to, double mag, String regionData) {
        try {
            DataBuffer.setAllData();
            int fr = Integer.parseInt(from.toString().replace("-", ""));
            int t = Integer.parseInt(to.toString().replace("-", ""));
            InputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int count = 0;
            boolean isWorldWide = false;
            if (regionData.equals("World Wide")) isWorldWide = true;
            while ((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                count++;
                if (count == 1) continue;
                String[] temp = line.split(",");
                Earchquake earchquake = new Earchquake();
                earchquake.setId((int) Integer.parseInt(temp[0]));
                earchquake.setDis_Date(temp[1]);
                earchquake.setLatitude(Double.parseDouble(temp[2]));
                earchquake.setLongitude(Double.parseDouble(temp[3]));
                earchquake.setDepth(Integer.parseInt(temp[4]));
                earchquake.setMagnitude(Double.parseDouble(temp[5]));
                earchquake.setRegion(temp[6]);
                if (earchquake.getDate() < fr || earchquake.getDate() > t || mag > earchquake.getMagnitude() || (!earchquake.getRegion().equals(regionData) && !isWorldWide))
                    continue;
                DataBuffer.getData().add(earchquake);
            }
            if (bufferedReader != null) bufferedReader.close();
            if (inputStream != null) inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getRegion() {
        region = new ArrayList<String>();
        try {
            InputStream inputStream = new FileInputStream(new File(ConfigLoader.csvLocation));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int count = 0;
            HashSet<String> regions = new HashSet<String>();
            while ((line = bufferedReader.readLine()) != null) {
                ++count;
                if (count == 1) continue;
                String[] temp = line.split(",");
                if (!regions.contains(temp[6])) {
                    regions.add(temp[6]);
                }
            }
            if (bufferedReader != null) bufferedReader.close();
            if (inputStream != null) inputStream.close();
            regions.iterator().forEachRemaining(e -> region.add(e.replace("\"", "")));
        } catch (Exception e) {
            System.out.println("Get region by csv file failed!");
        }
        return region;
    }
}
