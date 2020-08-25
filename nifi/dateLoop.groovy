class Const {
  static Date lastUpdate = null;
}

static onStart(ProcessContext context) {
  Const.lastUpdate = new Date('04/23/2020')
  println "onStart $context ${Const.lastUpdate}"
}

static onStop(ProcessContext context) {
  println "onStop $context ${Const.lastUpdate}"
}

d1 = Const.lastUpdate
d2 = new Date()
(d1..d2).each {
    def flowFile = session.create()
    flowFile = session.putAttribute(flowFile, 'day', it.format('dd'))
    flowFile = session.putAttribute(flowFile, 'month', it.format('MM'))
    flowFile = session.putAttribute(flowFile, 'year', it.format('yyyy'))
    flowFile = session.putAttribute(flowFile, 'targetDate', it.format('MM-dd-yyyy'))
    REL_SUCCESS << flowFile
}
Const.lastUpdate = d2