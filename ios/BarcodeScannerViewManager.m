#import "BarcodeScannerViewManager.h"
#import "BarcodeScannerView.h"
#import <React/RCTUIManager.h>

@implementation BarcodeScannerViewManager

RCT_EXPORT_MODULE(BarcodeScannerView)

- (UIView *)view {
    return [[BarcodeScannerView alloc] init];
}

// Exportar a propriedade onCodeScanned
RCT_EXPORT_VIEW_PROPERTY(onCodeScanned, RCTBubblingEventBlock)

// Exportar a propriedade onError
RCT_EXPORT_VIEW_PROPERTY(onError, RCTBubblingEventBlock)

// Exportar propriedades opcionais
RCT_EXPORT_VIEW_PROPERTY(showFrame, BOOL)
RCT_EXPORT_VIEW_PROPERTY(frameColor, NSString)

// Permitir que o React Native gerencie o cleanup
- (void)invalidate {
    // Chamado quando a view é removida
}

@end