import java.text.ParseException
import java.text.SimpleDateFormat

class DateConst {

  static SimpleDateFormat DATE_FORMAT_GMT = null;
  static def datePatterns = [
      'yyyy-MM-dd HH:mm:ss',
      'yyyy-MM-dd HH:mm',
      'yyyy-MM-dd',
      'MM/dd/yy HH:mm:ss',
      'MM/dd/yy HH:mm',
      'MM/dd/yy'
    ];

}

static onStart(ProcessContext context) {
  DateConst.DATE_FORMAT_GMT = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
  DateConst.DATE_FORMAT_GMT.setTimeZone(TimeZone.getTimeZone('UTC'))
}

def flowFile = session.get()
if (!flowFile) return

String last_update = flowFile.last_update
String parsedDate = last_update

for (f in DateConst.datePatterns) {
  try {
    SimpleDateFormat dateFormat = new SimpleDateFormat(f)
    dateFormat.setTimeZone(TimeZone.getTimeZone('UTC'))
    Date date = dateFormat.parse(last_update)
    parsedDate = DateConst.DATE_FORMAT_GMT.format(date)
    break
    } catch (ParseException pe) {
  //silent
  }
}
flowFile = session.putAttribute(flowFile, 'last_update', parsedDate)
session.transfer(flowFile, REL_SUCCESS)
