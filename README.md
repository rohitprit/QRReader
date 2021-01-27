# QRReader
1. Read a image having 4 qr codes with fixed position divide them exactly into equal parts and read the blackPixels in image and then decode the qrCode, then write them into a csv file
2. Change the Path in Constants class according to the path present
3. Do not change anything in application.yml for running in local environment
4. The code is directly runnable in linux, just go in folder target and run ./QRReader-0.0.1-SNAPSHOT.jar, go to browser and hit "http://localhost:8080/qrcode?fileName=imageName" and the csv will be created.
