import commands
import os
main = "./homography"
if os.path.exists(main):
    rc, out = commands.getstatusoutput("./homography ./Images/img.jpg")
    #print 'rc = %d, \nout = %s' % (rc, out)
 

