import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import javax.net.ssl.*
import java.io.*
import java.net.URL
import java.security.KeyStore

//get current flowfile
def flowFile = session.get()
if(!flowFile) return

//init parameters for https json post request from dynamucally added attributes of NIFI ExecuteScript processor 
def keyStoreFilename = keyStoreFilename.value
def keyStorePassword = keyStorePassword.value
def keyPassword = keyPassword.value
def keyStoreType = keyStoreType.value
def trustStoreFilename = trustStoreFilename.value
def trsutStorePassword = trustStorePassword.value
def trustStoreType = trustStoreType.value
def tlsProtocol = tlsProtocol.value
def url = httpsUrl.value
def jsonMessage = jsonMessage.value

try
{
  flowFile = session.write(flowFile,{inputStream,outputStream -> 
  } as StreamCallback
  
  //transfer control to success 
  session.transfer(flowFile,REL_SUCCESS)
}
catch(ex)
{
  flowFile = session.putAttribute(flowFile,"script_error_message",ex.getMessage())
  flowFile = session.putAttribute(flowFile,"script_error",e.toString())
  //transfer control to failure
  session.transfer(flowFile,REL_FAILURE)                        
}



