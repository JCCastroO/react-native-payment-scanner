#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import <React/RCTComponent.h>

@interface BarcodeScannerView : UIView <AVCaptureMetadataOutputObjectsDelegate>

@property (nonatomic, copy) RCTBubblingEventBlock onCodeScanned;

- (void)startScanning;
- (void)stopScanning;

@end