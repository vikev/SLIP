__author__ = 'kc'
'''
    Circula Moving average: requires no storage of data
    Adittions to be made division by shifting
'''

class Ma:
    def __init__(self):
        self.count = 0
        self.m = 3
    def m_avg(self, num):
        self.count = 4
        for i in range(0, len(num), 4):
            nums = sum(num[i:i+4])
            m_temp = self.m + nums - self.m/float(self.count)
            self.m = int(m_temp/float(self.count))
            print 'Circular moving average', self.m

    def run(self):
        m = Ma()
        l = [1,3,4,1,3,1,3,3,4,3,4,3,100,103,101,103,200,201,223,214]
        print 'simple avg', sum(l)/float(len(l))
        m.m_avg(l)

test = Ma()
test.run()