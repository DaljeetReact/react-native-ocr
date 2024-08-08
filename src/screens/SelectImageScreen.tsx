import React from 'react';
import {
  View,
  SafeAreaView,
  Image,
  StyleSheet,
  useWindowDimensions,
} from 'react-native';
import * as ImagePicker from 'react-native-image-picker';
import {ImagePickerResponse} from 'react-native-image-picker';
import {SelectScreenNavigationProps} from '../navigation/Navigator';
import * as routes from '../navigation/routes';
import {DemoButton, DemoResponse} from '../components/ui';

type SelectImageScreenProps = {
  navigation: SelectScreenNavigationProps;
};

export const SelectImageScreen = ({navigation}: SelectImageScreenProps) => {
  const {width} = useWindowDimensions();
  const [response, setResponse] = React.useState<ImagePickerResponse | null>(
    null,
  );

  const onButtonPress = React.useCallback(
    (
      type: 'capture' | 'library',
      options: ImagePicker.CameraOptions | ImagePicker.ImageLibraryOptions,
    ) => {
      if (type === 'capture') {
        ImagePicker.launchCamera(options, res => {
          if (res.didCancel || res.errorCode) {
            console.error('Camera error:', res.errorMessage);
            return;
          }
          setResponse(res);
        });
      } else {
        ImagePicker.launchImageLibrary(options, res => {
          if (res.didCancel || res.errorCode) {
            console.error('Image library error:', res.errorMessage);
            return;
          }
          setResponse(res);
        });
      }
    },
    [],
  );

  const onProcessImage = () => {
    const uri = response?.assets?.[0]?.uri;
    if (uri) {
      navigation.navigate(routes.PROCESS_IMAGE_SCREEN, {uri});
    } else {
      console.warn('No image selected');
    }
  };

  return (
    <View style={{flex: 1}}>
      <SafeAreaView style={{flex: 1, flexDirection: 'column-reverse'}}>
        <View style={{flexDirection: 'row', paddingBottom: 8}}>
          <DemoButton key="Process Image" onPress={onProcessImage}>
            {'Process Image'}
          </DemoButton>
        </View>
        <View style={{flexDirection: 'row', paddingVertical: 8}}>
          <DemoButton
            key="Take Image"
            onPress={() =>
              onButtonPress('capture', {
                saveToPhotos: true,
                mediaType: 'photo',
                includeBase64: false,
              })
            }>
            {'Take Image'}
          </DemoButton>
          <DemoButton
            key="Select Image"
            onPress={() =>
              onButtonPress('library', {
                selectionLimit: 0,
                mediaType: 'photo',
                includeBase64: false,
              })
            }>
            {'Select Image'}
          </DemoButton>
        </View>
        <View style={{paddingHorizontal: 8}}>
          <DemoResponse>
            {response ? JSON.stringify(response) : 'Nothing Found'}
          </DemoResponse>
        </View>
        {response?.assets?.[0]?.uri && (
          <View key={response.assets[0].uri} style={styles.image}>
            <Image
              resizeMode="cover"
              resizeMethod="scale"
              style={{width, height: width}}
              source={{uri: response.assets[0].uri}}
            />
          </View>
        )}
      </SafeAreaView>
    </View>
  );
};

const styles = StyleSheet.create({
  image: {
    marginVertical: 24,
    alignItems: 'center',
  },
});
