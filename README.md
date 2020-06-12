# QRReader
Read a image having 4 qr codes with fixed position divide them exactly into equal parts and read the blackPixels in image and then decode the qrCode, then write them into a cvs file
Change the Path in Constants class according to the path present
Do not change anything in application.yml for running in local environment
The code is directly runnable in linux, just go in folder target and run ./QRReader.jar go to browser hit "http://localhost:8080/qrcode?fileName=imageName" and the csv will be created.
