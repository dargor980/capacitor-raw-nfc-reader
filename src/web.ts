import { WebPlugin } from '@capacitor/core';

import type { RawNfcReaderPluginPlugin } from './definitions';

export class RawNfcReaderPluginWeb extends WebPlugin implements RawNfcReaderPluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
