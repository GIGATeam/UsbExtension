import os, sys, zipfile

catalog = "catalog.xml"
directory = "./library"
filename = ""
ex_from = "swc"
ex_to = "swc"

if os.path.isfile(catalog):
    sys.stdout.write('Please remove catalog.xml'+'\n')
    exit(0)

if len(sys.argv)<2:
    filename = "library.swc"
else:
    filename = sys.argv[1]
nfilename = filename.replace(ex_from, ex_to)
os.rename(filename, nfilename)

fh = open(nfilename, 'rb')
z = zipfile.ZipFile(fh)

for name in z.namelist():
    outpath = "./"
    z.extract(name, outpath)
fh.close()

os.remove(catalog)

