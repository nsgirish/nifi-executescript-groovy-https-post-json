import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import javax.net.ssl.*
import java.io.*
import java.net.URL
import java.security.KeyStore

//get current flowfile
def flowFile = session.get()
if(!flowFile) return

//init https post json parameters from dynamically added attributes of ExecuteScript processor
def keyStoreFilename = keyStoreFilename.value


