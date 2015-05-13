package uk.co.informaticslab;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Section;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import uk.co.informaticslab.data.DataRow;
import uk.co.informaticslab.utils.NetCDFUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Converts a Met Office NetCDF file to a CSV.
 */
public class NetcdfToCsv {

    private static final Logger LOG = LoggerFactory.getLogger(NetcdfToCsv.class);
    private static final DateTimeFormatter DTF = ISODateTimeFormat.dateTime().withZoneUTC();

    /**
     * Does all the conversion from .nc file to output CSV's
     *
     * Very specific to Met Office nc files, currently produces a single CSV file per
     * forecast timestep within the nc file.
     *
     * @param in NetCDF file to read in
     * @param outPrefix prefix of CSV files to output.
     */
    public static void convert(String in, String outPrefix) {
        LOG.info("Converting from file [{}] to file [{}]", in, outPrefix + "_x.csv");
        DateTime start = DateTime.now(DateTimeZone.UTC);

        NetcdfFile ncFile = null;
        BufferedWriter bw = null;
        try {
            ncFile = NetcdfFile.open(in);
            Variable coverageVariable = NetCDFUtils.getCoverageVariable(ncFile);

            LOG.debug("Getting dimensions...");
            double[][] dimensions = NetCDFUtils.extractDimensionValues(ncFile, coverageVariable);
            LOG.debug("Got dimensions...");

            double forecastRefTime = NetCDFUtils.getScalarVariablesValue(ncFile.findVariable("forecast_reference_time"));
            String forecastRefTimeValue = DTF.print(new DateTime(convertToMillis((long) forecastRefTime)));

            DataRow header = new DataRow();
            header.addData("forecast_reference_time");
            header.addData("time");
            header.addData("height");
            header.addData("latitude");
            header.addData("longitude");
            header.addData("datum");

            LOG.debug("Writing files...");
            int index = 0;
            int iIndex = 0;
            for (int i = 0; i < dimensions[0].length; i++) {
                iIndex++;
                String outFileName = outPrefix + "_" + iIndex + ".csv";
                LOG.debug("Writing rows to file {}...", outFileName);
                bw = new BufferedWriter(new FileWriter(new File(outFileName)));
                bw.write(header.getDataAsRow());

                String timeValue = DTF.print(new DateTime(convertToMillis((long) dimensions[0][i])));

                for (int j = 0; j < dimensions[1].length; j++) {

                    for (int k = 0; k < dimensions[2].length; k++) {

                        // allows us to fetch the entire lng range of values for a single lat value in 1 read.
                        Section section = new Section(new int[]{i, j, k, 0}, new int[]{1, 1, 1, dimensions[3].length});

                        Array data = coverageVariable.read(section);
                        float[] values = (float[]) data.get1DJavaArray(float.class);

                        for (int l = 0; l < values.length; l++) {
                            DataRow row = new DataRow();
                            row.addData(forecastRefTimeValue);
                            row.addData(timeValue);
                            row.addData(Double.toString(dimensions[1][j]));
                            row.addData(Double.toString(dimensions[2][k]));
                            row.addData(Double.toString(dimensions[3][l]));
                            row.addData(Float.toString(values[l]));

                            bw.write(row.getDataAsRow());
                            index++;
                        }
                    }
                }
                bw.flush();
                bw.close();
            }
            DateTime end = DateTime.now(DateTimeZone.UTC);
            LOG.info("Finished writing {} rows to {} files in {}s", index, iIndex, end.minus(start.getMillis()).getMillis() / 1000);

            ncFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long convertToMillis(long hours) {
        return hours * 60 * 60 * 1000;
    }

    private static String usage() {
        return new StringBuilder()
                .append("\n")
                .append("NetCDF to CSV usage:\n")
                .append("\tYou must supply 2 arguments...\n")
                .append("\t<INPUT FILE>\t\tthe NetCDF file to be read\n")
                .append("\t<OUTPUT FILE PREFIX>\tthe prefix to the output CSV files\n")
                .toString();
    }

    public static void main(String[] args) throws InvalidRangeException, IOException {
        if(args.length != 2) {
            System.out.println(usage());
            System.exit(-1);
        } else {
            convert(args[0], args[1]);
        }
    }

}