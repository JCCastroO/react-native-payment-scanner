#import "BarcodeScannerView.h"

@interface BarcodeScannerView()

@property (nonatomic, strong) AVCaptureSession *captureSession;
@property (nonatomic, strong) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, strong) AVCaptureMetadataOutput *metadataOutput;

@end

@implementation BarcodeScannerView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        [self setupCamera];
    }
    return self;
}

- (void)setupCamera {
    self.captureSession = [[AVCaptureSession alloc] init];
    
    AVCaptureDevice *videoCaptureDevice = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];
    NSError *error = nil;
    AVCaptureDeviceInput *videoInput = [AVCaptureDeviceInput deviceInputWithDevice:videoCaptureDevice error:&error];
    
    if (error) {
        NSLog(@"Error setting up camera: %@", error.localizedDescription);
        return;
    }
    
    if ([self.captureSession canAddInput:videoInput]) {
        [self.captureSession addInput:videoInput];
    }
    
    self.metadataOutput = [[AVCaptureMetadataOutput alloc] init];
    
    if ([self.captureSession canAddOutput:self.metadataOutput]) {
        [self.captureSession addOutput:self.metadataOutput];
        
        [self.metadataOutput setMetadataObjectsDelegate:self queue:dispatch_get_main_queue()];
        
        // Configurar tipos de código que queremos detectar
        self.metadataOutput.metadataObjectTypes = @[
            AVMetadataObjectTypeQRCode,
            AVMetadataObjectTypeEAN13Code,
            AVMetadataObjectTypeEAN8Code,
            AVMetadataObjectTypeCode128Code,
            AVMetadataObjectTypeCode39Code,
            AVMetadataObjectTypeUPCECode
        ];
    }
    
    self.previewLayer = [AVCaptureVideoPreviewLayer layerWithSession:self.captureSession];
    self.previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
    [self.layer addSublayer:self.previewLayer];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    self.previewLayer.frame = self.bounds;
}

- (void)startScanning {
    if (![self.captureSession isRunning]) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            [self.captureSession startRunning];
        });
    }
}

- (void)stopScanning {
    if ([self.captureSession isRunning]) {
        [self.captureSession stopRunning];
    }
}

- (void)didMoveToWindow {
    [super didMoveToWindow];
    if (self.window) {
        [self startScanning];
    } else {
        [self stopScanning];
    }
}

#pragma mark - AVCaptureMetadataOutputObjectsDelegate

- (void)captureOutput:(AVCaptureOutput *)output 
didOutputMetadataObjects:(NSArray<__kindof AVMetadataObject *> *)metadataObjects 
       fromConnection:(AVCaptureConnection *)connection {
    
    if (metadataObjects.count == 0 || !self.onCodeScanned) {
        return;
    }
    
    AVMetadataMachineReadableCodeObject *metadataObject = metadataObjects.firstObject;
    
    if ([metadataObject isKindOfClass:[AVMetadataMachineReadableCodeObject class]]) {
        NSString *barcodeType = [self getBarcodeType:metadataObject.type];
        
        self.onCodeScanned(@{
            @"data": metadataObject.stringValue ?: @"",
            @"type": barcodeType
        });
    }
}

- (NSString *)getBarcodeType:(AVMetadataObjectType)type {
    if ([type isEqualToString:AVMetadataObjectTypeQRCode]) return @"QR_CODE";
    if ([type isEqualToString:AVMetadataObjectTypeEAN13Code]) return @"EAN_13";
    if ([type isEqualToString:AVMetadataObjectTypeEAN8Code]) return @"EAN_8";
    if ([type isEqualToString:AVMetadataObjectTypeCode128Code]) return @"CODE_128";
    if ([type isEqualToString:AVMetadataObjectTypeCode39Code]) return @"CODE_39";
    if ([type isEqualToString:AVMetadataObjectTypeUPCECode]) return @"UPC_E";
    return @"UNKNOWN";
}

- (void)dealloc {
    [self stopScanning];
}

@end