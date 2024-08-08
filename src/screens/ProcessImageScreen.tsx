// ProcessImageScreen.tsx
import React, {useEffect, useState} from 'react';
import {Image, ScrollView, StyleSheet, useWindowDimensions} from 'react-native';
import {
  ProcessImageNavigationProps,
  ProcessImageRouteProps,
} from '../navigation/Navigator';
import {recognizeImage, Response} from '../mlkit';
import {ResponseRenderer} from '../components/ResponseRenderer'; // Ensure this path is correct

interface ProcessImageScreenProps {
  navigation: ProcessImageNavigationProps;
  route: ProcessImageRouteProps;
}

export const ProcessImageScreen = ({route}: ProcessImageScreenProps) => {
  const {width: windowWidth} = useWindowDimensions();
  const [aspectRatio, setAspectRatio] = useState(1);
  const [response, setResponse] = useState<Response | undefined>(undefined);
  const {uri} = route.params;

  useEffect(() => {
    if (uri) {
      processImage(uri);
    }
  }, [uri]);

  const processImage = async (url: string) => {
    try {
      const result = await recognizeImage(url);
      if (result?.blocks?.length > 0) {
        setResponse(result);
        const imageAspectRatio = (result.height || 1) / (result.width || 1);
        setAspectRatio(imageAspectRatio);
      }
    } catch (error) {
      console.error('Error recognizing image:', error);
    }
  };

  console.log(JSON.stringify(response,2,null));
  

  return (
    <ScrollView style={styles.container}>
      <Image
        source={{uri}}
        style={[styles.image, {height: windowWidth * aspectRatio}]}
        resizeMode="cover"
      />
      {response && (
        <ResponseRenderer
          response={response}
          scale={windowWidth / (response.width || 1)}
        />
      )}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  image: {
    width: '100%',
  },
});
