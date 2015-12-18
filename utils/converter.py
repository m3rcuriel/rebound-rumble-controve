import struct

with open("robot.log", "rb") as logfile:
    with open("robot.csv", "w") as out:
        header = logfile.read(3)
        if header != bytes("log", 'utf-8'):
            print("File format not recognized")
            exit()

        numberofelements = struct.unpack('b', logfile.read(1))[0]

        elementSizes = [None] * numberofelements

        for x in range(numberofelements):
            elementSizes[x] = struct.unpack('b', logfile.read(1))[0]

        for x in range(numberofelements):
            nameSize = struct.unpack('b', logfile.read(1))[0]
            name = logfile.read(nameSize).decode('utf-8')
            out.write(str(name) + ", ")

        out.write('\n')

        loc = logfile.tell();

        while logfile.read(4) != bytearray(b'\xFF\xFF\xFF\xFF'):
            logfile.seek(loc)
            for x in range(numberofelements):
                if elementSizes[x] == 4:
                    out.write(str(struct.unpack('>i', logfile.read(4))[0]) + ", ")
                elif elementSizes[x] == 2:
                    out.write(str(struct.unpack('>h', logfile.read(2))[0]) + ", ")
            out.write("\n")
            loc = logfile.tell()
        out.close()
        logfile.close()
