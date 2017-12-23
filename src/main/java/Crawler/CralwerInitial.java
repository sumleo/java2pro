package Crawler;

import Crawler.Utils.ConfigLoader;
import Crawler.Utils.UpdateData;

import java.util.Date;

public class CralwerInitial extends Thread {
    /**
     * CralwerInitial function
     */
    @Override
    public void run() {
        /**
         * Initial data
         */
        ConfigLoader.initial();
        try {
            while (true) {
                /**
                 * Start working
                 */
                System.out.println(new Date() + ">>>Update begins");
                UpdateData updateData = new UpdateData();
                updateData.update();
                updateData = null;
                System.out.println(new Date() + ">>>Update ends I will sleep for an hour.");
                /**
                 * Sleep for an hour
                 */
                Thread.sleep(1000 * 3600);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
