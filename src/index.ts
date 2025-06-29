import { registerPlugin } from '@capacitor/core';

import type { RawNfcReaderPluginPlugin } from './definitions';

const RawNfcReaderPlugin = registerPlugin<RawNfcReaderPluginPlugin>('RawNfcReaderPlugin', {
  web: () => import('./web').then((m) => new m.RawNfcReaderPluginWeb()),
});

export * from './definitions';
export { RawNfcReaderPlugin };
