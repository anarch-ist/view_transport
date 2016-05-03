package ru.logist.sbat.fileExecutor;

import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;
import ru.logist.sbat.GlobalUtils;
import ru.logist.sbat.jsonToBean.jsonReader.JsonPException;
import ru.logist.sbat.jsonToBean.jsonReader.ValidatorException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipInputStream;

public class FileToStringCmd {

    private static final String INCOMING_FILE_EXTENSION_ZIP = ".zip";
    private static final String INCOMING_FILE_EXTENSION_PKG = ".pkg";
    private Path filePath;

    public FileToStringCmd(Path filePath) {
        this.filePath = filePath;
    }

    public String execute() throws IOException, ValidatorException {
        Objects.requireNonNull(filePath);
        return getStringFromFile(filePath);
    }

    /**
     *
     * @param filePath
     * @return null if file was not imported
     * @throws ValidatorException
     * @throws JsonPException
     * @throws ParseException
     * @throws IOException
     */
    protected String getStringFromFile(Path filePath) throws ValidatorException, IOException {

        String result;
        if (filePath.toString().endsWith(INCOMING_FILE_EXTENSION_ZIP))
            result = readZipFileToUtf8String(filePath.toFile());
        else if (filePath.toString().endsWith(INCOMING_FILE_EXTENSION_PKG))
            result = FileUtils.readFileToString(filePath.toFile(), StandardCharsets.UTF_8);
        else
            throw new ValidatorException(GlobalUtils.getParameterizedString("file {} must end with {} or {} ,file will not be imported", filePath, INCOMING_FILE_EXTENSION_ZIP, INCOMING_FILE_EXTENSION_PKG));
        return result;
    }

    /**
     *
     * @return zip file with data as decompressed string
     */
    protected String readZipFileToUtf8String(File file) throws IOException {
        byte[] buffer = new byte[2048];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(file));
            // in our file should be only one entry
            zis.getNextEntry(); // get .pkg entry
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int len;
            while ((len = zis.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            return output.toString("UTF-8");
        }
        finally {
            if (zis != null) zis.close();
        }
    }

}
